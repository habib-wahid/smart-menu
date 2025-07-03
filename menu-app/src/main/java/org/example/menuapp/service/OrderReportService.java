package org.example.menuapp.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.menuapp.entity.Order;
import org.example.menuapp.repository.OrderRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderReportService {

    private final OrderRepository orderRepository;

    public OrderReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public String orderReport(String reportFormat) throws IOException, JRException {
        List<Order> orderList = orderRepository.findAll();
        ClassPathResource resource = new ClassPathResource("order-report.jrxml");
        InputStream inputStream = resource.getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orderList);
        Map<String, Object> map = new HashMap<>();
        map.put("report", "JasperReport");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
        if (reportFormat.equals("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/habibur/Documents/report/report.pdf");
        }
        return "report generated successfully";
    }
}
