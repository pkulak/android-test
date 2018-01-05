package com.pkulak.androidtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProductListResponse {
    public static String generate(int page, int perPage, int returned) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode node = mapper.valueToTree(ImmutableMap.builder()
                .put("id", UUID.randomUUID().toString())
                .put("totalProducts", 1467)
                .put("pageIndex", page)
                .put("pageSize", perPage)
                .put("status", 200)
                .put("kind", "test#resourcesItem")
                .put("etag", "YInXzt8HJdoVuUAZP-9wex9eVII/r48q7WTOsKLnD93m8m_1x1nP5OQ")
                .put("products", IntStream.range(0, Math.min(perPage, returned))
                        .mapToObj(i -> generateProduct())
                        .collect(Collectors.toList()))
                .build());

        return mapper.writeValueAsString(node);
    }

    public static ObjectNode generateProduct() {
        return new ObjectMapper().valueToTree(ImmutableMap.builder()
                .put("productId", UUID.randomUUID().toString())
                .put("productName", "Super Cool Product")
                .put("shortDescription", "It's cool, yo.")
                .put("longDescription", "Believe me, this is the greatest product. Just the best. Everyone is saying it. It's the best.")
                .put("price", "$2.95")
                .put("productImage", "https://i.imgur.com/d0Iav10.jpg")
                .put("reviewRating", 4.5)
                .put("reviewCount", 984)
                .put("inStock", new Random().nextBoolean())
                .build());
    }
}
