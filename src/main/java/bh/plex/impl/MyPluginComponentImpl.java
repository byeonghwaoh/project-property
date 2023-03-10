package bh.plex.impl;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import bh.plex.api.MyPluginComponent;
import com.atlassian.webresource.api.assembler.PageBuilderService;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService ({MyPluginComponent.class})
@Named ("myPluginComponent")
public class MyPluginComponentImpl implements MyPluginComponent
{
        @ComponentImport
        private final ApplicationProperties applicationProperties;

        @ComponentImport
        PageBuilderService pageBuilderService;

        @ComponentImport
        ActiveObjects activeObjects;

        @Inject
        public MyPluginComponentImpl(final ApplicationProperties applicationProperties)
        {
            this.applicationProperties = applicationProperties;
        }

        public String getName()
        {
            if(null != applicationProperties)
            {
                return "myComponent:" + applicationProperties.getDisplayName();
            }

            return "myComponent";
        }
}