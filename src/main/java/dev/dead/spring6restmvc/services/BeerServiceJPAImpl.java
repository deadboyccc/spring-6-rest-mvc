package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.entities.Beer;
import dev.dead.spring6restmvc.mappers.BeerMapper;
import dev.dead.spring6restmvc.models.BeerDTO;
import dev.dead.spring6restmvc.models.BeerStyle;
import dev.dead.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPAImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGE_SIZE = 25;
    private final CacheManager cacheManager;

    @Cacheable(cacheNames = "beerCache", key = "#beerId", unless = "#result == null")
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDTO(beerRepository.findById(id)
                .orElse(null)));
    }

    @Cacheable(cacheNames = "beerListCache")
    @Override
    public Page<BeerDTO> getBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber,
                                  Integer pageSize) {
        log.debug("Get Beers - JPA Impl of Beer Service");
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<Beer> beerPage;
        // query params
        if (StringUtils.hasText(beerName) && beerStyle == null) {
            beerPage = listBeersByBeerName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByBeerStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);

        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }
        // masking
        if (showInventory != null && !showInventory) {
            log.debug("Showing inventory - JPA Impl of Beer Service: {}", showInventory);
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }
        return beerPage.map(beerMapper::beerToBeerDTO);
    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize != null && pageSize > 0) {
            queryPageSize = Math.min(pageSize, 1800);
        } else {
            queryPageSize = DEFAULT_PAGE_SIZE;
        }

        return PageRequest.of(queryPageNumber, queryPageSize);
    }

    private Page<Beer> listBeersByBeerNameAndBeerStyle(String beerName, BeerStyle beerStyle, PageRequest pageRequest) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageRequest);
    }

    public Page<Beer> listBeersByBeerStyle(BeerStyle beerStyle, PageRequest pageRequest) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
    }

    public Page<Beer> listBeersByBeerName(String beerName, PageRequest pageRequest) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageRequest);
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        log.debug("Save New Beer - JPA Impl of Beer Service");
        Objects.requireNonNull(cacheManager.getCache("beerCache"))
                .evict(beerDTO.getId());
        Objects.requireNonNull(cacheManager.getCache("beerListCache"));
        return beerMapper.beerToBeerDTO(beerRepository.saveAndFlush(beerMapper.beerDTOToBeer(beerDTO)));
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID beerId, BeerDTO beerDTO) {
        Objects.requireNonNull(cacheManager.getCache("beerCache"))
                .evict(beerDTO.getId());
        Objects.requireNonNull(cacheManager.getCache("beerListCache"));
        return beerRepository.findById(beerId)
                .map(foundBeer -> {
                    foundBeer.setBeerName(beerDTO.getBeerName());
                    foundBeer.setBeerStyle(beerDTO.getBeerStyle());
                    foundBeer.setPrice(beerDTO.getPrice());
                    foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    foundBeer.setUpc(beerDTO.getUpc());
                    return beerMapper.beerToBeerDTO(beerRepository.saveAndFlush(foundBeer));
                });
    }

    @Override
    public Boolean deleteBeerById(UUID beerId) {
        Objects.requireNonNull(cacheManager.getCache("beerCache"))
                .evict(beerId);
        Objects.requireNonNull(cacheManager.getCache("beerListCache"));
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            beerRepository.flush();
            return true;
        }
        return false;

    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beerDTO) {
        Objects.requireNonNull(cacheManager.getCache("beerCache"))
                .evict(beerDTO.getId());
        Objects.requireNonNull(cacheManager.getCache("beerListCache"));
        return beerRepository.findById(beerId)
                .map(foundBeer -> {
                    if (beerDTO.getBeerName() != null && StringUtils.hasText(beerDTO.getBeerName())) {
                        foundBeer.setBeerName(beerDTO.getBeerName());
                    }
                    if (beerDTO.getBeerStyle() != null) {
                        foundBeer.setBeerStyle(beerDTO.getBeerStyle());
                    }
                    if (beerDTO.getPrice() != null) {
                        foundBeer.setPrice(beerDTO.getPrice());
                    }
                    if (beerDTO.getQuantityOnHand() != null) {
                        foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    }
                    if (beerDTO.getUpc() != null && StringUtils.hasText(beerDTO.getUpc())) {
                        foundBeer.setUpc(beerDTO.getUpc());
                    }
                    return beerMapper.beerToBeerDTO(beerRepository.saveAndFlush(foundBeer));
                });

    }
}
