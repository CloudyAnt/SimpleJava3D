package cn.itscloudy.sj3d.legacy;

import cn.itscloudy.sj3d.util.Range;

class _2DLineFunc {
    // dv = coefficient * iv + constant
    // iv = (dv - constant) / coefficient
    double coefficient;
    double constant;

    final boolean perpendicular; // perpendicular to iv (parallel to dv)
    final boolean parallel; // parallel to iv (perpendicular to dv)
    Range<Integer> dvRange; // range between dvA and dvB
    Range<Integer> ivRange; // range between ivA and ivB

    // create by 2 points
    // iv = independent variable, dv = dependent variable
    // for an x-y coordinate-system, iv = x, dv = y
    _2DLineFunc(double ivAD, double dvAD, double ivBD, double dvBD) {
        int ivA = (int) Math.round(ivAD);
        int dvA = (int) Math.round(dvAD);
        int ivB = (int) Math.round(ivBD);
        int dvB = (int) Math.round(dvBD);

        ivRange = new Range<>(ivA, ivB);
        dvRange = new Range<>(dvA, dvB);

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
            double height = dvA - dvB;
            double width = ivA - ivB;
            coefficient = height / width;
            constant = dvB - ivB * coefficient;
        } else {
            double height = dvB - dvA;
            double width = ivB - ivA;
            coefficient = height / width;
            constant = dvA - ivA * coefficient;
        }
    }

    double getDvByIv(double iv) {
        return iv * coefficient + constant;
    }

    Double getIvByDv(double dv) {
        if (perpendicular) {
            if (dv >= dvRange.getMin() && dv <= dvRange.getMax()) {
                return ivRange.getMin().doubleValue();
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
