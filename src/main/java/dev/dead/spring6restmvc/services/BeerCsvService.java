package dev.dead.spring6restmvc.services;

import dev.dead.spring6restmvc.models.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCsvService {
    List<BeerCSVRecord> convertToCSV(File csvFile);
}
