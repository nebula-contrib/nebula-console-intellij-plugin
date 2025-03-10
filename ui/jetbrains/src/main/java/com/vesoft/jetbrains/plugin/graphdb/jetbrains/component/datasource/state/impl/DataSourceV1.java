package com.vesoft.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.impl;

import com.vesoft.jetbrains.plugin.graphdb.jetbrains.component.datasource.DataSourceType;
import com.vesoft.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSourceApi;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataSourceV1 implements DataSourceApi {

    public String uuid = "notset";
    public String name = "unknown";
    public DataSourceType dataSourceType = DataSourceType.UNKNOWN;
    public Map<String, String> configuration = new HashMap<>();

    /**
     * Default constructor for serialization.
     */
    public DataSourceV1() {
    }

    public DataSourceV1(String uuid, String name, DataSourceType dataSourceType,
                        Map<String, String> configuration) {
        this.uuid = uuid;
        this.name = name;
        this.dataSourceType = dataSourceType;
        this.configuration = configuration;
    }

    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DataSourceType getDataSourceType() {
        return dataSourceType;
    }

    @Override
    public Map<String, String> getConfiguration() {
        return configuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSourceV1 that = (DataSourceV1) o;

        if (!Objects.equals(uuid, that.uuid)) return false;
        return dataSourceType == that.dataSourceType;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (dataSourceType != null ? dataSourceType.hashCode() : 0);
        return result;
    }
}
