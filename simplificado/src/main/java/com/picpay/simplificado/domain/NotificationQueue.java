package com.picpay.simplificado.domain;

import com.picpay.simplificado.domain.user.User;

public record NotificationQueue(User user, String message) { }
