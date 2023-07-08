package cn.itscloudy;

import cn.itscloudy.util.Range;

class _2DLineFunc {

    // y = coefficient * x + constant
    double coefficient;
    double constant;

    final boolean perpendicular;
    Range<Double> heightRange; // use this range to get points when it's perpendicular

    // create by 2 points
    // iv = independent variable, dv = dependent variable
    // for an x-y coordinate-system, iv = x, dv = y
    _2DLineFunc(double ivA, double dvA, double ivB, double dvB) {
        heightRange = new Range<>(dvA, dvB);
        if (dvA == dvB) {
            coefficient = 0;
            constant = dvA;
            perpendicular = false;
            return;
        }
        if (ivA == ivB) {
            perpendicular = true;
            return;
        }
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
