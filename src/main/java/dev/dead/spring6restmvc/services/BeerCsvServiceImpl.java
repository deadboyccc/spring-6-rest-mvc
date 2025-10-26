package dev.dead.spring6restmvc.services;

import com.opencsv.bean.CsvToBeanBuilder;
import dev.dead.spring6restmvc.models.BeerCSVRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Slf4j
@Service
public class BeerCsvServiceImpl implements BeerCsvService {
    @Override
    public List<BeerCSVRecord> convertToCSV(File csvFile) {
        log.debug("convertCSV() - Service");
        try {
            List<BeerCSVRecord> beerCSVRecords = new CsvToBeanBuilder<BeerCSVRecord>(new FileReader(csvFile))
                    .withType(BeerCSVRecord.class)
                    .build()
                    .parse();
            return beerCSVRecords;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
