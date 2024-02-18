package com.gf.springbatch.jobs;

import com.gf.springbatch.entities.Sale;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNamedQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;

@Configuration
public class Step5ReaderWriter {

    final Logger logger = LogManager.getLogger(this.getClass());

    @Bean
    public JpaCursorItemReader<Sale> step5JpaItemReader(EntityManagerFactory entityManagerFactory) {
        JpaNamedQueryProvider<Sale> jpaNamedQueryProvider = new JpaNamedQueryProvider<Sale>();
        jpaNamedQueryProvider.setNamedQuery("Sale.findAll");
        jpaNamedQueryProvider.setEntityManager(entityManagerFactory.createEntityManager());
        return new JpaCursorItemReaderBuilder<Sale>()
                .entityManagerFactory(entityManagerFactory)
                .name("step5JpaItemReader")
                .queryProvider(jpaNamedQueryProvider)
                .build();
    }

    @Bean
    public ItemWriter<Sale> step5ItemWriter(EntityManagerFactory entityManagerFactory) {
        return new ItemWriter<Sale>() {
            @Override
            public void write(Chunk<? extends Sale> chunk) throws Exception {
                chunk.getItems().forEach(elem -> {
                    logger.info(elem.toString());
                });
            }
        };
    }

    @Bean
    public FlatFileItemWriter<Sale> step5FileItemWriter(EntityManagerFactory entityManagerFactory) throws IOException {
        BeanWrapperFieldExtractor<Sale> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"id", "product", "type", "quantity", "unitPrice"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<Sale> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        File file = new File("sales.csv");
        try {
            if (file.createNewFile()) {
                logger.debug("File creato con successo.");
            } else {
                logger.warn("Il file esiste già.");
            }
        } catch (IOException e) {
            logger.error("Errore durante la creazione del file: " + e.getMessage());
            throw e;
        }

        return new FlatFileItemWriterBuilder<Sale>()
                .name("step5FileItemWriter")
                .resource(new FileSystemResource(file))
                .shouldDeleteIfExists(true)
                .lineAggregator(lineAggregator)
                .build();
    }

}
