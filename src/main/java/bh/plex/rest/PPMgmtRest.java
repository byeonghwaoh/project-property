package bh.plex.rest;

import bh.plex.ao.Property;
import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * A resource
 */
@Path("/property")
public class PPMgmtRest {
    private static final Logger log = LoggerFactory.getLogger("com.ben");
    private ActiveObjects activeObjects;

    @Inject
    public PPMgmtRest(ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }



    @GET
    @Path("/{projectId}/getAll")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll(@PathParam("projectId") final String projectId)
    {
        Property[] properties = activeObjects.executeInTransaction(new TransactionCallback<Property[]>() {

            @Override
            public Property[] doInTransaction() {
                // 해당 프로젝트ID를 가진 모든 프러퍼티 조회
                Property[] propArray = activeObjects.find(Property.class, Query.select().where("PRJ_ID = ?", projectId));

                return propArray;
            }
        });
        List<PPMgmtRestModel> props = new ArrayList<PPMgmtRestModel>();

        // Rest 리턴 값을 위해 조회한 데이터를 Model 리스트에 추가
        if(properties != null) {
            for(Property property : properties) {
                props.add(new PPMgmtRestModel(property.getID(), property.getPrjId()
                        , property.getPKey(), property.getPValue()));
            }
        }

        return Response.ok(props).build();
    }







    @GET
    @Path("/{projectId}/self/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getProperty(@PathParam("projectId") final String projectId, @PathParam("id") final Integer id)
    {
        Property[] properties =  activeObjects.executeInTransaction(new TransactionCallback<Property[]>() {

            @Override
            public Property[] doInTransaction() {
                // ID에 해당하는 프로퍼티 행 조회
                return activeObjects.find(Property.class, Query.select().where("ID = ?", id));
            }
        });

        // Rest에서 사용할 모델로 변환
        PPMgmtRestModel pModel = new PPMgmtRestModel();
        if(properties != null && properties.length > 0) {
            for(Property prop : properties) {
                pModel.setId(prop.getID());
                pModel.setPrjId(prop.getPrjId());
                pModel.setpKey(prop.getPKey());
                pModel.setpValue(prop.getPValue());
            }
        }

        return Response.ok(pModel).build();
    }



    @PUT
    @Path("/{projectId}/self/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response editProperty(@PathParam("projectId") final String projectId, @PathParam("id") final Integer id, PPMgmtRestModel pModel)
    {
        PPMgmtRestModel editPModel = activeObjects.executeInTransaction(new TransactionCallback<PPMgmtRestModel>() {
            @Override
            public PPMgmtRestModel doInTransaction() {
                // ID에 해당하는 행 조회
                PPMgmtRestModel model = new PPMgmtRestModel();
                Property[] props = activeObjects.find(Property.class, Query.select().where("ID = ?", id));

                // 프로퍼티 행을 찾은 경우 뷰에서 입력받은 값으로 변경
                if(props != null && props.length >0) {
                    for(Property prop : props) {
                        if(pModel.getPrjId() != null) {
                            prop.setPrjId(pModel.getPrjId());
                        }
                        if(pModel.getpKey() != null) {
                            prop.setPKey(pModel.getpKey());
                        }
                        if(pModel.getpValue() != null) {
                            prop.setPValue(pModel.getpValue());
                        }
                        prop.save();
                        model.setId(prop.getID());
                        model.setPrjId(prop.getPrjId());
                        model.setpKey(prop.getPKey());
                        model.setpValue(prop.getPValue());
                    }
                }
                return model;
            }
        });

        return Response.ok(editPModel).build();
    }




    @DELETE
    @Path("/{projectId}/self/{id}")
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteProperty(@PathParam("projectId") final String projectId,
                                   @PathParam("id") final Integer id)
    {
        activeObjects.executeInTransaction(new TransactionCallback<Integer>() {

            @Override
            public Integer doInTransaction() {
                // ID에 해당하는 프로퍼티 행 조회
                Property[] props = activeObjects.find(Property.class, Query.select().where("ID = ?", id));

                if(props != null && props.length >0) {
                    for(Property prop : props) {
                        // 삭제 처리
                        activeObjects.delete(prop);
                    }
                }

                return id;
            }
        });

        return Response.ok().build();
    }




    @POST
    @Path("/{projectId}/self")
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON})
    public Response createProperty(@PathParam("projectId") final String projectId, PPMgmtRestModel pModel)
    {
        Property property =  activeObjects.executeInTransaction(new TransactionCallback<Property>() {
            @Override
            public Property doInTransaction() {
                // 입력받은 새로은 프로퍼티 행을 추가
                Property property = activeObjects.create(Property.class);
                property.setPrjId(projectId);
                property.setPKey(pModel.getpKey());
                property.setPValue(pModel.getpValue());
                property.save();

                return property;
            }
        });

        // 추가할 프로퍼티 모델 리턴
        PPMgmtRestModel newPModel = new PPMgmtRestModel();
        newPModel.setId(property.getID());
        newPModel.setPrjId(property.getPrjId());
        newPModel.setpKey(property.getPKey());
        newPModel.setpValue(property.getPValue());

        return Response.ok(newPModel).build();
    }


}