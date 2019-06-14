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

import com.arjuna.webservices11.wsat.sei.CoordinatorPortTypeImpl;
import com.arjuna.webservices11.wscoor.sei.RegistrationPortTypeImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class WebServiceConfig {

    @Autowired
    private Bus bus;

    @Bean
    public Endpoint registration() {
        EndpointImpl endpoint = new EndpointImpl(bus, new RegistrationPortTypeImpl());
        endpoint.publish("/ws-c11/RegistrationService");
        return endpoint;
    }

    @Bean
    public Endpoint coordinator() {
        EndpointImpl endpoint = new EndpointImpl(bus, new CoordinatorPortTypeImpl());
        endpoint.publish("/ws-t11-coordinator/CoordinatorService");
        return endpoint;

    }
}
