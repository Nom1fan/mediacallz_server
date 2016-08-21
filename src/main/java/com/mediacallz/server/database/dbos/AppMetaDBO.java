package com.mediacallz.server.database.dbos;

import java.io.Serializable;

/**
 * Created by Mor on 26/03/2016.
 */
public class AppMetaDBO implements Serializable {

    private final double _minSupportedVersion;

    public AppMetaDBO(double _minSupportedVersion) {
        this._minSupportedVersion = _minSupportedVersion;
    }

    public double get_minSupportedVersion() {
        return _minSupportedVersion;
    }

    @Override
    public String toString() {

        return "[MinSupportedVersion]:" + _minSupportedVersion;
    }
}
