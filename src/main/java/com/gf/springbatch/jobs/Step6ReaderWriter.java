package com.gf.springbatch.jobs;

import com.gf.springbatch.entities.Sale;
import com.gf.springbatch.entities.TypeSale;
import jakarta.persistence.EntityManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.validation.BindException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class Step6ReaderWriter {

    final Logger logger = LogManager.getLogger(this.getClass());

    Map<String, List<Sale>> groupedData = new ConcurrentHashMap<>();

    @Bean
    public FlatFileItemReader<Sale> step6FileItemReader(EntityManagerFactory entityManagerFactory) {
        FlatFileItemReader<Sale> saleFlatFileItemReader = new FlatFileItemReader<>();
        saleFlatFileItemReader.setResource(new FileSystemResource("sales.csv"));

        DefaultLineMapper<Sale> saleLineMapper = new DefaultLineMapper<>();
        saleLineMapper.setLineTokenizer(new DelimitedLineTokenizer());
        saleLineMapper.setFieldSetMapper(new FieldSetMapper<Sale>() {
            @Override
            public Sale mapFieldSet(FieldSet fieldSet) throws BindException {
                Sale sale = new Sale();

                sale.setId(fieldSet.readLong(0));
                sale.setProduct(fieldSet.readString(1));
                sale.setType(fieldSet.readString(2));
                sale.setQuantity(fieldSet.readInt(3));
                sale.setUnitPrice(Double.parseDouble(fieldSet.readString(4)));

                return sale;
            }
        });
        saleFlatFileItemReader.setLineMapper(saleLineMapper);
        saleFlatFileItemReader.open(new ExecutionContext());

        return saleFlatFileItemReader;
    }

    @Bean
    public ItemProcessor<Sale, TypeSale> step6ItemProcessor(EntityManagerFactory entityManagerFactory) {
        return new ItemProcessor<>() {
            @Override
            public TypeSale process(Sale item) throws Exception {
                TypeSale typeSale = new TypeSale();

                typeSale.setType(item.getType());
                typeSale.setTotalPrice(item.getUnitPrice());
                typeSale.setTotalQuantity(item.getQuantity());

                return typeSale;
            }
        };
    }

    @Bean
    public ListItemWriter<TypeSale> step6ItemWriter() {
        return new ListItemWriter<>();
    }

}
