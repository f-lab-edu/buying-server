package org.example.buyingserver.payment.client;

import org.example.buyingserver.payment.domain.PGProvider;
import org.example.buyingserver.payment.exception.UnsupportedPaymentProviderException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentClientRouter {

    private final Map<PGProvider, PaymentClient> clients;

    public PaymentClientRouter(List<PaymentClient> clientList) {
        this.clients = clientList.stream()
                .collect(Collectors.toMap(PaymentClient::getProvider, Function.identity()));
    }

    public PaymentClient route(PGProvider provider) {
        PaymentClient client = clients.get(provider);
        if (client == null) {
            throw new UnsupportedPaymentProviderException();
        }
        return client;
    }
}