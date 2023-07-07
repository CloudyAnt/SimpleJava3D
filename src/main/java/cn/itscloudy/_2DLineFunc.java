package cn.itscloudy;

import cn.itscloudy.util.Range;

class _2DLineFunc {

    // y = coefficient * x + constant
    double coefficient;
    double constant;

    final boolean perpendicular;
    Range<Double> heightRange; // use this range to get points when it's perpendicular

    // create by 2 points
    // for an x-y coordinate-system, active = x, passive = y
    _2DLineFunc(double aActive, double aPassive, double bActive, double bPassive) {
        heightRange = new Range<>(aPassive, bPassive);
        if (aPassive == bPassive) {
            coefficient = 0;
            constant = aPassive;
            perpendicular = false;
            return;
        }
        if (aActive == bActive) {
            perpendicular = true;
            return;
        }
        perpendicular = false;

        if (aActive > bActive) {
            double height = aPassive - bPassive;
            double width = aActive - bActive;
            coefficient = height / width;
            constant = bPassive - bActive * coefficient;
        } else {
            double height = bPassive - aPassive;
            double width = bActive - aActive;
            coefficient = height / width;
            constant = aPassive - aActive * coefficient;
        }
    }

    double getByParam(double param) {
        return param * coefficient + constant;
    }

}
