package mii.bsi.apiportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mii.bsi.apiportal.domain.ServiceApiDomain;
import mii.bsi.apiportal.repository.ServiceApiRepository;

@Service
public class ApiService {
    @Autowired
    LogService logService;
    @Autowired
    ServiceApiRepository serviceApiRepository;

}
