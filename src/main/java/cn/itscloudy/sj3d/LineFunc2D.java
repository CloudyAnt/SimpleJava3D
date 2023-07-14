package cn.itscloudy.sj3d;

import cn.itscloudy.sj3d.util.Range;

public class LineFunc2D {
    public final Range<Float> dvRange; // range between dvA and dvB
    public final Range<Float> ivRange; // range between ivA and ivB

    public final Range<Integer> dvIRange; // range between dvA and dvB
    public final Range<Integer> ivIRange; // range between ivA and ivB

    // dv = coefficient * iv + constant
    // iv = (dv - constant) / coefficient
    public float coefficient;
    public float constant;

    public final boolean perpendicular; // perpendicular to iv (parallel to dv)
    public final boolean parallel; // parallel to iv (perpendicular to dv)

    // create by 2 points
    // iv = independent variable, dv = dependent variable
    // for an x-y coordinate-system, iv = x, dv = y
    public LineFunc2D(float ivAF, float dvAF, float ivBF, float dvBF) {
        // to eliminate slight deviation, use rounded int here
        int ivA = Math.round(ivAF);
        int dvA = Math.round(dvAF);
        int ivB = Math.round(ivBF);
        int dvB = Math.round(dvBF);

        ivRange = new Range<>(ivAF, ivBF);
        dvRange = new Range<>(dvAF, dvBF);

        ivIRange = new Range<>(ivA, ivB);
        dvIRange = new Range<>(dvA, dvB);

        if (ivA == ivB) {
            parallel = false;
            perpendicular = true;
            return;
        }

        if (dvA == dvB) {
            constant = dvA;
            parallel = true;
            perpendicular = false;
            return;
        }

        parallel = false;
        perpendicular = false;

        if (ivA > ivB) {
            float height = dvA - dvB;
            float width = ivA - ivB;
            coefficient = height / width;
            constant = dvB - ivB * coefficient;
        } else {
            float height = dvB - dvA;
            float width = ivB - ivA;
            coefficient = height / width;
            constant = dvA - ivA * coefficient;
        }
    }


    /**
     * Calculate dependent value calculated by independent value.
     */
    public float getDvByIv(float iv) {
        return iv * coefficient + constant;
    }

    /**
     * Calculate independent value calculated by dependent value. <br/>
     * Null if coefficient is 0 or dependent value out of range.
     */
    public Float getIvByDv(float dv) {
        if (perpendicular) {
            if (dv >= dvRange.getMin() && dv <= dvRange.getMax()) {
                return ivRange.getMin();
            } else {
                return null;
            }
        }
        if (coefficient == 0) {
            return null;
        }
        return (dv - constant) / coefficient;
    }

}
