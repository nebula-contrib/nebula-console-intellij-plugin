package com.vesoft.jetbrains.plugin.graphdb.test.integration.neo4j.tests.database.common;

import com.vesoft.jetbrains.plugin.graphdb.jetbrains.component.datasource.metadata.DataSourceMetadata;
import com.vesoft.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSourceApi;
import com.vesoft.jetbrains.plugin.graphdb.test.integration.neo4j.data.StoredProcedure;
import com.vesoft.jetbrains.plugin.graphdb.test.integration.neo4j.util.base.BaseIntegrationTest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static com.vesoft.jetbrains.plugin.graphdb.jetbrains.component.datasource.metadata.Neo4jBoltCypherDataSourceMetadata.STORED_PROCEDURES;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public abstract class AbstractDataSourceMetadataTest extends BaseIntegrationTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        component().dataSources().refreshAllMetadata();
    }

    public abstract DataSourceApi getDataSource();

    public void testMetadataExists() throws ExecutionException, InterruptedException {
        Optional<DataSourceMetadata> metadata = component().dataSourcesMetadata().getMetadata(getDataSource(),null).get();
        assertThat(metadata).isPresent();
    }

    public void testMetadataHaveRequiredProcedures() {
        DataSourceMetadata metadata = getMetadata();
        List<Map<String, String>> storedProcedures = metadata.getMetadata(STORED_PROCEDURES);

        List<Map<String, String>> requiredProcedures = requiredProcedures().stream()
                .map(StoredProcedure::asMap)
                .collect(Collectors.toList());
        assertThat(storedProcedures)
                .containsAll(requiredProcedures);
    }

    protected abstract List<StoredProcedure> requiredProcedures();

    protected StoredProcedure procedure(final String name, final String signature, final String description, final String mode) {
        return new StoredProcedure(name, signature, description, mode);
    }

    protected StoredProcedure procedure(final String name, final String signature, final String description, final String mode, final String worksOnSystem) {
        return new StoredProcedure(name, signature, description, mode, worksOnSystem);
    }

    protected DataSourceMetadata getMetadata() {

        try {
            CompletableFuture<Optional<DataSourceMetadata>> futureMeta = component().dataSourcesMetadata().getMetadata(getDataSource());
            return futureMeta.get(30, TimeUnit.SECONDS)
                    .orElseThrow(() -> new IllegalStateException("Metadata should not be null"));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("Failed to retrieve metadata", e);
        }
    }
}
