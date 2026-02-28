package org.example.batch;

import javax.sql.DataSource;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    // 1. THE READER: Read the CSV line by line
    @Bean
    public FlatFileItemReader<Project> reader() {
        return new FlatFileItemReaderBuilder<Project>()
                .name("projectReader")
                .resource(new ClassPathResource("projects-data.csv"))
                .delimited()
                .names("title", "description") // Matches the CSV columns
                .targetType(Project.class)     // Converts to our Java object
                .build();
    }

    // 2. THE PROCESSOR: Clean the data (Capitalize the title)
    @Bean
    public ItemProcessor<Project, Project> processor() throws InterruptedException {
        Thread.sleep(1000);
        return project -> {
            String upperCaseTitle = project.getTitle().toUpperCase();
            project.setTitle(upperCaseTitle);
            return project;

        };

    }

    // 3. THE WRITER: Output the data
    // (For this basic portfolio, we write to the console. In a real app, you'd use JdbcBatchItemWriter to save to a Database)
    // 1. REPLACE your old writer() with this one
    @Bean
    public JdbcBatchItemWriter<Project> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Project>()
                // This maps Java properties to SQL parameters automatically
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                // The SQL query to execute for each item
                .sql("INSERT INTO project (title, description) VALUES (:title, :description)")
                // The database connection (Spring Boot provides this automatically)
                .dataSource(dataSource)
                .build();
    }

    // 4. THE STEP: Combine Reader, Processor, and Writer
    @Bean
    public Step importStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, JdbcBatchItemWriter<Project> writer) throws InterruptedException {
        return new StepBuilder("importStep", jobRepository)
                .<Project, Project>chunk(2, transactionManager) // Process in chunks of 2 rows at a time
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    // 5. THE JOB: The actual task to run
    @Bean
    public Job importProjectJob(JobRepository jobRepository, Step importStep) {
        return new JobBuilder("importProjectJob", jobRepository)
                .start(importStep)
                .build();
    }
}
