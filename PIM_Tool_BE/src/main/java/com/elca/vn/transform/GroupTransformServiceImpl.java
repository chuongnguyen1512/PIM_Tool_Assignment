package com.elca.vn.transform;

import com.elca.vn.entity.Group;
import com.elca.vn.proto.model.PimGroup;

import java.util.Objects;

/**
 * Transform service for pair models {@link PimGroup} and {@link Group}
 */
public class GroupTransformServiceImpl implements BaseTransformService<PimGroup, Group> {

    /**
     * {@inheritDoc}
     *
     * @param sourceObject input object
     * @return
     */
    @Override
    public Group transformFromSourceToDes(PimGroup sourceObject) {
        if (Objects.isNull(sourceObject)) {
            return null;
        }
        Group group = new Group();
        group.setGroupID(sourceObject.getGroupID());
        group.setGroupName(sourceObject.getGroupName());
        return group;
    }

    /**
     * {@inheritDoc}
     *
     * @param destinationObject input object
     * @return
     */
    @Override
    public PimGroup transformFromDesToSource(Group destinationObject) {
        if (Objects.isNull(destinationObject)) {
            return null;
        }
        return PimGroup.newBuilder()
                .setGroupID(destinationObject.getGroupID())
                .setGroupName(destinationObject.getGroupName())
                .build();
    }
}
