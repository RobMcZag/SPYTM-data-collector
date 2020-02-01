package com.robertozagni.SPYTM.data.collector.repository;

import com.robertozagni.SPYTM.data.collector.model.TimeSerie;
import com.robertozagni.SPYTM.data.collector.model.TimeSerieMetadata;
import org.springframework.data.repository.CrudRepository;

public interface TimeSerieMetadataRepository extends CrudRepository<TimeSerieMetadata, TimeSerie.TimeSerieId> {
}
