package cn.itscloudy;

import cn.itscloudy.util.Range;

class _2DLineFunc {

    // y = coefficient * x + constant
    double coefficient;
    double constant;

    final boolean perpendicular;
    final boolean parallel;
    Range<Integer> heightRange; // use this range to get points when it's perpendicular

    // create by 2 points
    // iv = independent variable, dv = dependent variable
    // for an x-y coordinate-system, iv = x, dv = y
    _2DLineFunc(double ivAD, double dvAD, double ivBD, double dvBD) {
        int ivA = (int) ivAD;
        int dvA = (int) dvAD;
        int ivB = (int) ivBD;
        int dvB = (int) dvBD;

        heightRange = new Range<>(dvA, dvB);

        if (ivA == ivB) {
            parallel = false;
            perpendicular = true;
            return;
        }

        if (dvA == dvB) {
            coefficient = 0;
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

    double getDvByIv(double param) {
        return param * coefficient + constant;
    }

}
