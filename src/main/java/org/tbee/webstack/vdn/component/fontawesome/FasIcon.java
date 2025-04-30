package org.tbee.webstack.vdn.component.fontawesome;

import org.tbee.webstack.vdn.component.ExternalIcon;

import java.util.ArrayList;
import java.util.List;

/// You have to include a personal license link in the application.
/// ```
/// @JavaScript("https://kit.fontawesome.com/....js")
/// ```
/// https://fontawesome.com
public class FasIcon extends ExternalIcon<FasIcon> {

    // https://fontawesome.com/search?o=r&s=solid&ip=classic
    public FasIcon(String iconName) {
        this("fa-solid", iconName);
    }

    public FasIcon(String familyName, String iconName) {
        addClassNames(familyName, iconName);
    }
}
