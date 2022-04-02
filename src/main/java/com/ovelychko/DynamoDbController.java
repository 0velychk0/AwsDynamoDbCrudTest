package com.ovelychko;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class DynamoDbController {
    private static final Logger logger = LoggerFactory.getLogger(DynamoDbController.class);

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @GetMapping("/createTable")
    public String createTable() {
        CreateTableRequest req = dynamoDBMapper.generateCreateTableRequest(ProductCatalog.class);
        req.setProvisionedThroughput(new ProvisionedThroughput(2L, 2L));
        amazonDynamoDB.createTable(req);
        logger.info("Creation done");
        return "Table created";
    }

    @GetMapping("/deleteTable")
    public String deleteTable() {
        DeleteTableRequest req = dynamoDBMapper.generateDeleteTableRequest(ProductCatalog.class);
        amazonDynamoDB.deleteTable(req);
        logger.info("Deletion done");
        return "Table deleted";
    }

    @GetMapping("/addItem")
    public String addItem(@RequestParam String id) {
        ProductCatalog ar = new ProductCatalog();
        ar.setId(Integer.parseInt(id));
        ar.setISBN("ISBN_" + id);
        ar.setPageCount(33);
        ar.setPrice(44);
        ar.setTitle("TITLE_" + id);
        ar.setInPublication(false);
        dynamoDBMapper.save(ar);
        logger.info("Adding done: {}", id);
        return "Item '" + id + "' added";
    }

    @GetMapping("/getItem")
    public String getItem(@RequestParam String id) {
        ProductCatalog itemRetrieved = dynamoDBMapper.load(ProductCatalog.class, Integer.parseInt(id));
        logger.info("Query done for: {}", id);
        return (itemRetrieved != null) ? itemRetrieved.toString() : ("item '" + id + "' not found");
    }

    @GetMapping("/getAll")
    public String getAll() {
        Map<String, AttributeValue> eav = new HashMap<>();
//        eav.put(":val1", new AttributeValue().withN(value));
//        eav.put(":val2", new AttributeValue().withS("Book"));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
//                .withFilterExpression("Price < :val1 and ProductCategory = :val2").withExpressionAttributeValues(eav);

        List<ProductCatalog> scanResult = dynamoDBMapper.scan(ProductCatalog.class, scanExpression);

        logger.info("Query done: {}", Arrays.toString(scanResult.toArray()));
        return Arrays.toString(scanResult.toArray());
    }

    @GetMapping("/")
    public String test() {
        return "Service Active";
    }
}
