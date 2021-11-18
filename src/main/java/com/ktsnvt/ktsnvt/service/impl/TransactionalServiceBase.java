package com.ktsnvt.ktsnvt.service.impl;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(isolation = Isolation.READ_COMMITTED)
public abstract class TransactionalServiceBase {
}
