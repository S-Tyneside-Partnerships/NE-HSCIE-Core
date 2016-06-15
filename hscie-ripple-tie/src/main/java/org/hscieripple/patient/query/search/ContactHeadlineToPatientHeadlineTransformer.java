/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.hscieripple.patient.query.search;

import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.contacts.model.ContactHeadline;
import org.rippleosi.patient.summary.model.PatientHeadline;

/**
 */
public class ContactHeadlineToPatientHeadlineTransformer implements Transformer<ContactHeadline, PatientHeadline> {

    @Override
    public PatientHeadline transform(ContactHeadline input) {

        PatientHeadline headline = new PatientHeadline();
        headline.setSource(input.getSource());
        headline.setSourceId(input.getSourceId());
        headline.setText(input.getName());

        return headline;
    }
}