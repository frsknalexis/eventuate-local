package io.eventuate.local.unified.cdc.pipeline.common.factory;

import io.eventuate.javaclient.spring.jdbc.EventuateSchema;
import io.eventuate.local.common.*;
import io.eventuate.local.java.common.broker.DataProducerFactory;
import io.eventuate.local.unified.cdc.pipeline.common.BinlogEntryReaderProvider;
import io.eventuate.local.unified.cdc.pipeline.common.properties.CdcPipelineProperties;
import org.apache.curator.framework.CuratorFramework;

abstract public class CommonCdcPipelineFactory<EVENT extends BinLogEvent> implements CdcPipelineFactory<EVENT> {

  protected CuratorFramework curatorFramework;
  protected DataProducerFactory dataProducerFactory;
  protected BinlogEntryReaderProvider binlogEntryReaderProvider;


  public CommonCdcPipelineFactory(CuratorFramework curatorFramework,
                                  DataProducerFactory dataProducerFactory,
                                  BinlogEntryReaderProvider binlogEntryReaderProvider) {
    this.curatorFramework = curatorFramework;
    this.dataProducerFactory = dataProducerFactory;
    this.binlogEntryReaderProvider = binlogEntryReaderProvider;
  }

  protected abstract SourceTableNameSupplier createSourceTableNameSupplier(CdcPipelineProperties cdcPipelineProperties);

  protected abstract PublishingStrategy<EVENT> createPublishingStrategy();

  protected EventTableChangesToAggregateTopicTranslator<EVENT> createEventTableChangesToAggregateTopicTranslator(CdcDataPublisher<EVENT> cdcDataPublisher,
                                                                                                                 CdcProcessor<EVENT> cdcProcessor) {


    return new EventTableChangesToAggregateTopicTranslator<>(cdcDataPublisher, cdcProcessor);
  }

  protected EventuateSchema createEventuateSchema(CdcPipelineProperties properties) {
    return new EventuateSchema(properties.getEventuateDatabaseSchema());
  }

  protected abstract BinlogEntryToEventConverter<EVENT> createBinlogEntryToEventConverter();
}