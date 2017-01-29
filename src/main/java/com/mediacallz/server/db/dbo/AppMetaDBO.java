package com.mediacallz.server.db.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Mor on 26/03/2016.
 */
@Data
@AllArgsConstructor
public class AppMetaDBO extends DBOEntity {

    private double last_supported_version;
}
