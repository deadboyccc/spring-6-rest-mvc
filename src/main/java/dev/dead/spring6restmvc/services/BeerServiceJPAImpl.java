package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.entities.Beer;
import dev.dead.spring6restmvc.events.BeerCreatedEvent;
import dev.dead.spring6restmvc.mappers.BeerMapper;
import dev.dead.spring6restmvc.models.BeerDTO;
import dev.dead.spring6restmvc.models.BeerStyle;
import dev.dead.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BeerServiceJPAImpl implements BeerService {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final String BEER_CACHE = "beerCache";
    private static final String BEER_LIST_CACHE = "beerListCache";

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Cacheable(cacheNames = BEER_CACHE, key = "#id", unless = "#result == null")
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDTO);
    }

    @Cacheable(cacheNames = BEER_LIST_CACHE)
    @Override
    public Page<BeerDTO> getBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, 
                                  Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<Beer> beerPage;
        
        if (StringUtils.hasText(beerName) && beerStyle == null) {
            beerPage = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(
                    "%" + beerName + "%", beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }
        
        if (Boolean.FALSE.equals(showInventory)) {
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }
        
        return beerPage.map(beerMapper::beerToBeerDTO);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = BEER_LIST_CACHE, allEntries = true)
    })
    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        Beer savedBeer = beerRepository.saveAndFlush(beerMapper.beerDTOToBeer(beerDTO));
        
        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .ifPresent(auth -> applicationEventPublisher.publishEvent(
                        new BeerCreatedEvent(savedBeer, auth)));
        
        return beerMapper.beerToBeerDTO(savedBeer);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = BEER_CACHE, key = "#beerId"),
            @CacheEvict(cacheNames = BEER_LIST_CACHE, allEntries = true)
    })
    @Override
    public Optional<BeerDTO> updateBeer(UUID beerId, BeerDTO beerDTO) {
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

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = BEER_CACHE, key = "#beerId"),
            @CacheEvict(cacheNames = BEER_LIST_CACHE, allEntries = true)
    })
    @Override
    public Boolean deleteBeerById(UUID beerId) {
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            beerRepository.flush();
            return true;
        }
        return false;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = BEER_CACHE, key = "#beerId"),
            @CacheEvict(cacheNames = BEER_LIST_CACHE, allEntries = true)
    })
    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beerDTO) {
        return beerRepository.findById(beerId)
                .map(foundBeer -> {
                    if (StringUtils.hasText(beerDTO.getBeerName())) foundBeer.setBeerName(beerDTO.getBeerName());
                    if (beerDTO.getBeerStyle() != null) foundBeer.setBeerStyle(beerDTO.getBeerStyle());
                    if (beerDTO.getPrice() != null) foundBeer.setPrice(beerDTO.getPrice());
                    if (beerDTO.getQuantityOnHand() != null) foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    if (StringUtils.hasText(beerDTO.getUpc())) foundBeer.setUpc(beerDTO.getUpc());
                    
                    return beerMapper.beerToBeerDTO(beerRepository.saveAndFlush(foundBeer));
                });
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber = (pageNumber != null && pageNumber > 0) ? pageNumber - 1 : DEFAULT_PAGE;
        int queryPageSize = (pageSize != null && pageSize > 0) ? Math.min(pageSize, 1000) : DEFAULT_PAGE_SIZE;
        return PageRequest.of(queryPageNumber, queryPageSize);
    }
}
