package com.pagamentos.simplificado.domain;

import com.pagamentos.simplificado.domain.user.User;

public record NotificationQueue(User user, String message) { }
