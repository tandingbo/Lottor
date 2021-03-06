package com.blueskykong.lottor.core.service.impl;

import com.blueskykong.lottor.common.bean.TxTransactionInfo;
import com.blueskykong.lottor.common.concurrent.threadlocal.CompensationLocal;
import com.blueskykong.lottor.common.enums.OperationEnum;
import com.blueskykong.lottor.common.helper.SpringBeanUtils;
import com.blueskykong.lottor.core.service.AspectTransactionService;
import com.blueskykong.lottor.core.service.TxTransactionFactoryService;
import com.blueskykong.lottor.core.service.TxTransactionHandler;
import org.springframework.beans.factory.annotation.Autowired;


public class AspectTransactionServiceImpl implements AspectTransactionService {

    private final TxTransactionFactoryService txTransactionFactoryService;

    @Autowired
    public AspectTransactionServiceImpl(TxTransactionFactoryService txTransactionFactoryService) {
        this.txTransactionFactoryService = txTransactionFactoryService;
    }

    @Override
    public void invoke(String transactionGroupId, Object[] args, OperationEnum operationEnum) {

        final String compensationId = CompensationLocal.getInstance().getCompensationId();

        TxTransactionInfo info = new TxTransactionInfo(null, args, transactionGroupId, compensationId, 60, operationEnum);
        final Class c = txTransactionFactoryService.factoryOf(info);
        final TxTransactionHandler txTransactionHandler =
                (TxTransactionHandler) SpringBeanUtils.getInstance().getBean(c);
        txTransactionHandler.handler(info);
    }
}
