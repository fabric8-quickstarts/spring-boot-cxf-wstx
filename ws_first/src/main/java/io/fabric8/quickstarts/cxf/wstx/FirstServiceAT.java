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

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "FirstServiceAT", targetNamespace = "http://service.ws.sample/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface FirstServiceAT {

    /**
     * Create a new booking
     */
    @WebMethod
    public void incrementCounter(int numSeats);

    /**
     * obtain the number of existing bookings
     *
     * @return the number of current bookings
     */
    @WebMethod
    public int getCounter();

    /**
     * Reset the booking count to zero
     */
    @WebMethod
    public void resetCounter();

}


