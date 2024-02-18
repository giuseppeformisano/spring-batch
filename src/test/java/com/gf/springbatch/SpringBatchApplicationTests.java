package com.gf.springbatch;

import com.gf.springbatch.entities.TypeSale;
import com.gf.springbatch.entities.TypeSaleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@SpringBatchTest
class SpringBatchApplicationTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    ListItemWriter<TypeSale> step6ItemWriter;

    @Autowired
    TypeSaleRepository typeSaleRepository;

    @Test
    public void testJob(@Autowired Job job) throws Exception {
        this.jobLauncherTestUtils.setJob(job);
        JobParameters jobParameters = new JobParametersBuilder()
                .addJobParameter("errorStep2", true, Boolean.class)
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        Map<String, List<TypeSale>> typeSaleMap = step6ItemWriter.getWrittenItems().stream()
                .collect(Collectors.groupingBy(TypeSale::getType));

        List<TypeSale> typeSales = typeSaleMap.entrySet().stream().map(entry -> {
            TypeSale typeSale = new TypeSale();
            typeSale.setType(entry.getKey());
            typeSale.setTotalPrice(entry.getValue().stream().mapToDouble(TypeSale::getTotalPrice).sum());
            typeSale.setTotalQuantity(entry.getValue().stream().mapToInt(TypeSale::getTotalQuantity).sum());
            return typeSale;
        }).toList();

        typeSaleRepository.deleteAll();
        typeSaleRepository.saveAll(typeSales);

        Assertions.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }

}
