package com.elca.vn.fragment;

import org.jacpfx.api.annotations.fragment.Fragment;
import org.jacpfx.api.fragment.Scope;

import static com.elca.vn.configuration.UIConfiguration.PROJECT_LIST_FRAGMENT_FXML_URL;
import static com.elca.vn.configuration.UIConfiguration.PROJECT_LIST_FRAGMENT_ID;

/**
 * Fragment for listing projects
 */
@Fragment(id = PROJECT_LIST_FRAGMENT_ID,
        viewLocation = PROJECT_LIST_FRAGMENT_FXML_URL,
        scope = Scope.PROTOTYPE)
public class ProjectListFragment {

}
