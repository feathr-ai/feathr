package com.linkedin.frame.core.utils;

import com.linkedin.frame.common.urn.MlFeatureUrn;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.frame.common.urn.TupleKey;
import com.linkedin.frame.core.config.producer.common.FeatureRef;
import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.common.TypedRef;
import java.util.Objects;

/**
 * Util class to convert {@link MlFeatureVersionUrn} back to {@link FeatureRef}
 * It is a sister class of {@link MlFeatureVersionUrnCreator}
 */
public class FeatureRefConverter {
    private static final FeatureRefConverter INSTANCE = new FeatureRefConverter();

    private static final int ML_FEATURE_URN_INDEX = 0;
    private static final int MAJOR_VERSION_INDEX = 1;
    private static final int MINOR_VERSION_INDEX = 2;
    private static final int NAMESPACE_INDEX = 0;
    private static final int FEATURE_NAME_INDEX = 1;

    public static FeatureRefConverter getInstance() {
        return INSTANCE;
    }

    private FeatureRefConverter() {
    }

    public FeatureRef getFeatureRef(MlFeatureVersionUrn mlFeatureVersionUrn) {
        String featureRefString = getFeatureRefStr(mlFeatureVersionUrn);
        return new FeatureRef(featureRefString);
    }

    public String getFeatureRefStr(MlFeatureVersionUrn mlFeatureVersionUrn) {
        TupleKey versionEntityKey = mlFeatureVersionUrn.getEntityKey();
        MlFeatureUrn mlFeatureUrn = versionEntityKey.getAs(ML_FEATURE_URN_INDEX, MlFeatureUrn.class);
        int major = versionEntityKey.getAs(MAJOR_VERSION_INDEX, Integer.class);
        int minor = versionEntityKey.getAs(MINOR_VERSION_INDEX, Integer.class);
        TupleKey mlFeatureUrnEntityKey = mlFeatureUrn.getEntityKey();
        String namespace = mlFeatureUrnEntityKey.getAs(NAMESPACE_INDEX, String.class);
        String featureName = mlFeatureUrnEntityKey.getAs(FEATURE_NAME_INDEX, String.class);

        StringBuilder featureRefBuilder = new StringBuilder();
        if (!Objects.equals(namespace, Namespace.DEFAULT_NAMESPACE_OBJ.toString())) {
            featureRefBuilder.append(namespace).append(TypedRef.DELIM);
        }
        featureRefBuilder.append(featureName);
        if (major != 0 || minor != 0) {
            featureRefBuilder.append(TypedRef.DELIM).append(major).append(TypedRef.DELIM).append(minor);
        }
        return featureRefBuilder.toString();
    }
}
