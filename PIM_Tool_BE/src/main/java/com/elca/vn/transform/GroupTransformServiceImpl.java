package com.elca.vn.transform;

import com.elca.vn.entity.Group;
import com.elca.vn.proto.model.PimGroup;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Transform service for pair models {@link PimGroup} and {@link Group}
 */
@Service
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
        group.setId(sourceObject.getGroupID());
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
                .setGroupID(destinationObject.getId())
                .setGroupName(destinationObject.getGroupName())
                .build();
    }
}
