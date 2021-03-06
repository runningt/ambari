/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing privileges and
 * limitations under the License.
 */

package org.apache.ambari.server.api.services.views;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.ambari.server.api.resources.ResourceInstance;
import org.apache.ambari.server.api.services.BaseService;
import org.apache.ambari.server.api.services.Request;
import org.apache.ambari.server.controller.ViewPrivilegeResponse;
import org.apache.ambari.server.controller.spi.Resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 *  Service responsible for view privilege resource requests.
 */
@Api(tags = "Views", description = "Endpoint for view specific operations")
@Path("/views/{viewName}/versions/{version}/instances/{instanceName}/privileges")
public class ViewPrivilegeService extends BaseService {
  /**
   * Handles: GET  /views/{viewName}/versions/{version}/instances/{instanceName}/privileges
   * Get all privileges.
   *
   * @param headers       http headers
   * @param ui            uri info
   * @param viewName      view id
   * @param version       version id
   * @param instanceName  instance id
   *
   * @return privilege collection representation
   */
  @GET
  @Produces("text/plain")
  @ApiOperation(value = "Get all view instance privileges", nickname = "ViewPrivilegeService#getPrivileges", notes = "Returns all privileges for the resource.", response = ViewPrivilegeResponse.class, responseContainer = "List")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "fields", value = "Filter privileges", defaultValue = "PrivilegeInfo/*", dataType = "string", paramType = "query"),
    @ApiImplicitParam(name = "sortBy", value = "Sort privileges (asc | desc)", defaultValue = "PrivilegeInfo/user_name.asc", dataType = "string", paramType = "query"),
    @ApiImplicitParam(name = "page_size", value = "The number of resources to be returned for the paged response.", defaultValue = "10", dataType = "integer", paramType = "query"),
    @ApiImplicitParam(name = "from", value = "The starting page resource (inclusive). Valid values are :offset | \"start\"", defaultValue = "0", dataType = "string", paramType = "query"),
    @ApiImplicitParam(name = "to", value = "The ending page resource (inclusive). Valid values are :offset | \"end\"", dataType = "string", paramType = "query")
  })
  public Response getPrivileges(@Context HttpHeaders headers, @Context UriInfo ui,
                                @ApiParam(value = "view name") @PathParam("viewName") String viewName,
                                @ApiParam(value = "view version") @PathParam("version") String version,
                                @ApiParam(value = "instance name") @PathParam("instanceName") String instanceName) {
    return handleRequest(headers, null, ui, Request.Type.GET, createPrivilegeResource(viewName, version, instanceName,null));
  }

  /**
   * Handles: GET /views/{viewName}/versions/{version}/instances/{instanceName}/privileges/{privilegeID}
   * Get a specific privilege.
   *
   * @param headers        http headers
   * @param ui             uri info
   * @param viewName       view id
   * @param version        version id
   * @param instanceName   instance id
   * @param privilegeId    privilege id
   *
   * @return privilege instance representation
   */
  @GET
  @Path("/{privilegeId}")
  @Produces("text/plain")
  @ApiOperation(value = "Get single view instance privilege", nickname = "ViewPrivilegeService#getPrivilege", notes = "Returns privilege details.", response = ViewPrivilegeResponse.class)
  @ApiImplicitParams({
    @ApiImplicitParam(name = "fields", value = "Filter privilege details", defaultValue = "PrivilegeInfo", dataType = "string", paramType = "query")
  })
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successful operation", response = ViewPrivilegeResponse.class)}
  )
  public Response getPrivilege(@Context HttpHeaders headers, @Context UriInfo ui,
                               @ApiParam(value = "view name") @PathParam("viewName") String viewName,
                               @ApiParam(value = "view version") @PathParam("version") String version,
                               @ApiParam(value = "instance name") @PathParam("instanceName") String instanceName,
                               @ApiParam(value = "privilege id", required = true) @PathParam("privilegeId") String privilegeId) {

    return handleRequest(headers, null, ui, Request.Type.GET, createPrivilegeResource(viewName, version, instanceName,privilegeId));
  }

  /**
   * Handles: POST /views/{viewName}/versions/{version}/instances/{instanceName}/privileges
   * Create a privilege.
   *
   * @param body          request body
   * @param headers       http headers
   * @param ui            uri info
   * @param viewName      view id
   * @param version       version id
   * @param instanceName  instance id
   *
   * @return information regarding the created privilege
   */
  @POST
  @Produces("text/plain")
  @ApiOperation(value = "Create view instance privilege", nickname = "ViewPrivilegeService#createPrivilege", notes = "Create privilege resource for view instance.")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "body", value = "input parameters in json form", required = true, dataType = "org.apache.ambari.server.controller.ViewPrivilegeRequest", paramType = "body")
  })
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successful operation"),
    @ApiResponse(code = 500, message = "Server Error")}
  )
  public Response createPrivilege(String body, @Context HttpHeaders headers, @Context UriInfo ui,
                                  @ApiParam(value = "view name") @PathParam("viewName") String viewName,
                                  @ApiParam(value = "view version") @PathParam("version") String version,
                                  @ApiParam(value = "instance name") @PathParam("instanceName") String instanceName) {

    return handleRequest(headers, body, ui, Request.Type.POST, createPrivilegeResource(viewName, version, instanceName,null));
  }

  /**
   * Handles: PUT /views/{viewName}/versions/{version}/instances/{instanceName}/privileges/{privilegeID}
   * Update a specific privilege.
   *
   * @param headers       http headers
   * @param ui            uri info
   * @param viewName      view id
   * @param version       version id
   * @param instanceName  instance id
   * @param privilegeId   privilege id
   *
   * @return information regarding the updated privilege
   */
  @PUT
  // Remove comments when the below API call is fixed
  /*@Path("{privilegeId}")
  @Produces("text/plain")
  @ApiOperation(value = "Update view instance privilege", notes = "Update privilege resource for view instance.")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "body", value = "input parameters in json form", required = true, dataType = "org.apache.ambari.server.controller.ViewPrivilegeRequest", paramType = "body")
  })
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successful operation"),
    @ApiResponse(code = 500, message = "Server Error")}
  ) */
  public Response updatePrivilege(String body, @Context HttpHeaders headers, @Context UriInfo ui,
                                  @ApiParam(value = "view name") @PathParam("viewName") String viewName,
                                  @ApiParam(value = "view version") @PathParam("version") String version,
                                  @ApiParam(value = "instance name") @PathParam("instanceName") String instanceName,
                                  @ApiParam(value = "privilege id") @PathParam("privilegeId") String privilegeId) {

    return handleRequest(headers, body, ui, Request.Type.PUT, createPrivilegeResource(viewName, version, instanceName, privilegeId));
  }

  /**
   * Handles: PUT /views/{viewName}/versions/{version}/instances/{instanceName}/privileges
   * Update a set of privileges for the resource.
   *
   * @param body          request body
   * @param headers       http headers
   * @param ui            uri info
   * @param viewName      view id
   * @param version       version id
   * @param instanceName  instance id
   *
   * @return information regarding the updated privileges
   */
  @PUT
  @Produces("text/plain")
  public Response updatePrivileges(String body, @Context HttpHeaders headers, @Context UriInfo ui,
                                   @ApiParam(value = "view name") @PathParam("viewName") String viewName,
                                   @ApiParam(value = "view version") @PathParam("version") String version,
                                   @ApiParam(value = "instance name") @PathParam("instanceName") String instanceName) {
    return handleRequest(headers, body, ui, Request.Type.PUT, createPrivilegeResource(viewName, version, instanceName,null));
  }

  /**
   * Handles: DELETE /views/{viewName}/versions/{version}/instances/{instanceName}/privileges
   * Delete privileges.
   *
   * @param body          request body
   * @param headers       http headers
   * @param ui            uri info
   * @param viewName      view id
   * @param version       version id
   * @param instanceName  instance id
   *
   * @return information regarding the deleted privileges
   */
  @DELETE
  @Produces("text/plain")
  public Response deletePrivileges(String body, @Context HttpHeaders headers, @Context UriInfo ui,
                                   @ApiParam(value = "view name") @PathParam("viewName") String viewName,
                                   @ApiParam(value = "view version") @PathParam("viewVersion") String version,
                                   @ApiParam(value = "instance name") @PathParam("instanceName") String instanceName) {

    return handleRequest(headers, body, ui, Request.Type.DELETE, createPrivilegeResource(viewName, version, instanceName,null));
  }

  /**
   * Handles: DELETE /views/{viewName}/versions/{version}/instances/{instanceName}/privileges/{privilegeID}
   * Delete a specific privilege.
   *
   * @param headers       http headers
   * @param ui            uri info
   * @param viewName      view id
   * @param version       version id
   * @param instanceName  instance id
   * @param privilegeId   privilege id
   *
   * @return information regarding the deleted privilege
   */
  @DELETE
  @Path("{privilegeId}")
  @Produces("text/plain")
  @ApiOperation(value = "Delete view instance privilege", nickname = "ViewPrivilegeService#deletePrivilege", notes = "Delete view instance privilege resource.")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successful operation"),
    @ApiResponse(code = 500, message = "Server Error")}
  )
  public Response deletePrivilege(@Context HttpHeaders headers, @Context UriInfo ui,
                                  @ApiParam(value = "view name") @PathParam("viewName") String viewName,
                                  @ApiParam(value = "view version") @PathParam("version") String version,
                                  @ApiParam(value = "instance name") @PathParam("instanceName") String instanceName,
                                  @ApiParam(value = "privilege id") @PathParam("privilegeId") String privilegeId) {

    return handleRequest(headers, null, ui, Request.Type.DELETE, createPrivilegeResource(viewName, version, instanceName, privilegeId));
  }


  protected ResourceInstance createPrivilegeResource(String viewName, String viewVersion, String instanceName, String privilegeId) {
    Map<Resource.Type,String> mapIds = new HashMap<>();
    mapIds.put(Resource.Type.View, viewName);
    mapIds.put(Resource.Type.ViewVersion, viewVersion);
    mapIds.put(Resource.Type.ViewInstance, instanceName);
    mapIds.put(Resource.Type.ViewPrivilege, privilegeId);

    return createResource(Resource.Type.ViewPrivilege, mapIds);
  }
}

