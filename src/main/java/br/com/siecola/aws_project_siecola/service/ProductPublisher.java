package br.com.siecola.aws_project_siecola.service;

import br.com.siecola.aws_project_siecola.entity.Product;
import br.com.siecola.aws_project_siecola.enums.EventType;
import br.com.siecola.aws_project_siecola.model.Envelope;
import br.com.siecola.aws_project_siecola.model.ProductEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class ProductPublisher {

    private static final Logger LOG = LoggerFactory
            .getLogger(ProductPublisher.class);

    private AmazonSNS snsCliente;
    private Topic productEventsTopic;
    private ObjectMapper objectMapper;



    public ProductPublisher(AmazonSNS snsCliente,
                            @Qualifier("productEventsTopic") Topic productEventsTopic, ObjectMapper objectMapper) {
        this.snsCliente = snsCliente;
        this.productEventsTopic = productEventsTopic;
        this.objectMapper = objectMapper;
    }

    public void PublisherProductEvent(Product product, EventType eventType, String username) {
        ProductEvent productEvent = new ProductEvent();
        productEvent.setProductId(product.getId());
        productEvent.setCode(product.getCode());
        productEvent.setUsername(username);

        Envelope envelope = new Envelope();
        envelope.setEventType(eventType);

        try {
            envelope.setData(objectMapper.writeValueAsString(productEvent));

            PublishResult publishResult =  snsCliente.publish(
                    productEventsTopic.getTopicArn(),
                    objectMapper.writeValueAsString(envelope));

            LOG.info("Product event send - Event: {} - ProductId: {} - MessageId: {}  ", envelope.getEventType(), productEvent.getProductId(), publishResult.getMessageId());


        } catch (JsonProcessingException e) {
           LOG.error("Error to create produt event message");
        }

    }

}
