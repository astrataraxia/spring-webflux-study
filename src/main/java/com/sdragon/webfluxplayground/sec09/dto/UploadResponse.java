package com.sdragon.webfluxplayground.sec09.dto;

import java.util.UUID;

public record UploadResponse (UUID confirmationId,
                             Long productsCount){
}
