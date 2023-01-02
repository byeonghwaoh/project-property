package bh.plex.tab;

import com.atlassian.crowd.embedded.api.Group;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.plugin.projectpanel.ProjectTabPanel;
import com.atlassian.jira.plugin.projectpanel.impl.AbstractProjectTabPanel;
import com.atlassian.jira.project.browse.BrowseContext;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;


public class PPMgmtTabPanel extends AbstractProjectTabPanel implements ProjectTabPanel
{
    private static final Logger log = LoggerFactory.getLogger("com.ben");

    @Inject
    private PageBuilderService pageBuilderService;

    public void setPageBuilderService(PageBuilderService pageBuilderService) {
        this.pageBuilderService = pageBuilderService;
    }

    public boolean showPanel(BrowseContext context)
    {
        // 현재 로그인 유저명 조회
        String userName = context.getUser().getName();
        String adminGroupName = "jira-administrators";
        // jira-administrators 그룹 조회
        Group group = ComponentAccessor.getGroupManager().getGroup(adminGroupName);

        // 현재 사용자가 어드민 그룹에 속한 경우만 표시(true)
        if(ComponentAccessor.getGroupManager().getGroupsForUser(userName).contains(group)) {
            return true;
        }

        // 일반 유저일 경우 미표시(false)
        return false;
    }


    @Override
    protected Map<String, Object> createVelocityParams(BrowseContext ctx) {
        // 뷰 화면에서 참조할 패러미터 맵 정의
        Map<String,Object> params = super.createVelocityParams(ctx);

        // 패러미터 맵에 사용자명과 프로젝트ID 추가
        params.put("userName", ctx.getUser().getName());
        params.put("projectId", ctx.getProject().getId());
        log.debug("params=" + params);

        // 뷰 화면에서 리소스로 지정한 파일들을 사용할 수 있도록 선언
        pageBuilderService.assembler().resources().requireWebResource("bh.plex.ppmgmt:ppmgmt-resources");

        return params;
    }
}
