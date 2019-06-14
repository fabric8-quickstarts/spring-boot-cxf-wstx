/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2019, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package io.fabric8.quickstarts.cxf.wstx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@WebService(serviceName = "FirstServiceATService", portName = "FirstServiceAT", name = "FirstServiceAT", targetNamespace = "http://service.ws.sample")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@HandlerChain(file = "/wstx_handlers.xml")
@Transactional(Transactional.TxType.MANDATORY) // default is REQUIRED
public class FirstServiceATImpl implements FirstServiceAT {

    private static final Integer ENTITY_ID = 1;

    private static final Logger LOG = Logger.getLogger(FirstServiceATImpl.class.getName());

    @Autowired
    private FirstCounterRepository service;

    /**
     * Incriment the first counter. This is done by updating the counter within a JTA transaction. The JTA transaction
     * was automatically bridged from the WS-AT transaction.
     */
    @WebMethod
    public void incrementCounter(int num) {

        LOG.info("[SERVICE] First service invoked to increment the counter by '" + num + "'");

        // invoke the backend business logic:
        LOG.info("[SERVICE] Using the JPA Entity Manager to update the counter within a JTA transaction");

        FirstCounter counter = lookupCounterEntity();
        counter.incrementCounter(num);
        service.save(counter);
    }

    @WebMethod
    public int getCounter() {
        LOG.info("[SERVICE] getCounter() invoked");
        FirstCounter counter = lookupCounterEntity();

        return counter.getCounter();
    }

    @WebMethod
    public void resetCounter() {
        FirstCounter counter = lookupCounterEntity();
        counter.setCounter(0);
        service.save(counter);
    }

    private FirstCounter lookupCounterEntity() {
        Optional<FirstCounter> counter = service.findById(ENTITY_ID);
        if (counter.isPresent()) {
            return counter.get();
        } else {
            FirstCounter first = new FirstCounter(ENTITY_ID, 0);
            service.save(first);
            return first;
        }
    }
}
