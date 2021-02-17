package com.uni.bankuni.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResult<T> {

    private T result;
}
