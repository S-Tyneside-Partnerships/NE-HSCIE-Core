/*
 * Copyright 2015 Ripple OSI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hscieripple.patient.allergies.search;

import javax.xml.ws.soap.SOAPFaultException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hscieripple.patient.allergies.AllergiesDetailsResponse;
import org.hscieripple.patient.allergies.AllergiesSummaryResponse;
import org.hscieripple.patient.allergies.AllergyServiceSoap;
import org.hscieripple.patient.allergies.PairOfAllergiesListKeyAllergiesSummaryResultRow;
import org.hscieripple.common.service.AbstractHSCIEService; 
import org.hscieripple.patient.datasources.model.DataSourceSummary;
import org.hscieripple.patient.allergies.model.HSCIEAllergyDetails;
import org.hscieripple.patient.allergies.model.HSCIEAllergySummary;
import org.hscieripple.patient.allergies.search.HSCIEAllergySearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HSCIEAllergiesSearch extends AbstractHSCIEService implements HSCIEAllergySearch {

    private static final Logger log = LoggerFactory.getLogger(HSCIEAllergiesSearch.class);
    
    private static final String OK = "OK";

    @Autowired
    private AllergyServiceSoap allergiesService;

    @Override
    public List<HSCIEAllergySummary> findAllAllergies(String patientId, List<DataSourceSummary> datasourceSummaries) {
        List<HSCIEAllergySummary> allergies = new ArrayList<>();

        Long nhsNumber = convertPatientIdToLong(patientId);

        for (DataSourceSummary summary : datasourceSummaries) {
            List<HSCIEAllergySummary> results = makeSummaryCall(nhsNumber, summary.getSourceId());

            allergies.addAll(results);
        }

        return allergies;
    }

    @Override
    public HSCIEAllergyDetails findAllergy(String patientId, String allergyId, String source) {
        AllergiesDetailsResponse response = new AllergiesDetailsResponse();

        Long nhsNumber = convertPatientIdToLong(patientId);

        try {
            response = allergiesService.findAllergiesDetailsBO(nhsNumber, allergyId, source);

            if (!isSuccessfulDetailsResponse(response)) {
                return new HSCIEAllergyDetails();
            }
        }
        catch (SOAPFaultException e) {
            log.error(e.getMessage(), e);
        }

        return new AllergiesDetailsResponseToDetailsTransformer().transform(response);
    }

    private List<HSCIEAllergySummary> makeSummaryCall(Long nhsNumber, String source) {
        List<PairOfAllergiesListKeyAllergiesSummaryResultRow> results = new ArrayList<>();

        try {
            AllergiesSummaryResponse response = allergiesService.findAllergiesSummariesBO(nhsNumber, source);

            if (isSuccessfulSummaryResponse(response)) {
                results = response.getAllergiesList().getAllergiesSummaryResultRow();
            }
        }
        catch (SOAPFaultException e) {
            log.error(e.getMessage(), e);
        }

        return CollectionUtils.collect(results, new AllergiesResponseToAllergiesSummaryTransformer(), new ArrayList<>());
    }

    private boolean isSuccessfulSummaryResponse(AllergiesSummaryResponse response) {
        return OK.equalsIgnoreCase(response.getStatusCode()); 
    }

    private boolean isSuccessfulDetailsResponse(AllergiesDetailsResponse response) {
        return OK.equalsIgnoreCase(response.getStatusCode()); 
    }
}