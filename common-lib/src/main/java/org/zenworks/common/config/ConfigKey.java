package org.zenworks.common.config;

public enum ConfigKey {

    ZOOKEEPER_FAVORITES("zkFav"),
    REDIS_FAVORITES("redisFav"),
    REDIS_INITIAL_WATCH_LIST("redisWatchList");

    private String cliParam;

    private ConfigKey(final String cliParam) {
        this.cliParam=cliParam;
    }

    public String getCliParam() { return cliParam; }

}
