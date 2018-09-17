#include "squarejacobimatrix3d.h"

SquareJacobiMatrix3d::SquareJacobiMatrix3d() {
}

SquareJacobiMatrix3d::SquareJacobiMatrix3d(std::function<Vector3d (Vector3d)> f, Vector3d p) {
    //TODO:
}

SquareJacobiMatrix3d &SquareJacobiMatrix3d::inverse() {
    //TODO:
    return *this;
}

Vector3d SquareJacobiMatrix3d::operator*(const Vector3d &rhd) {
    Vector3d result;
    for (std::size_t k = 0; k < 3; k++) {
        for(std::size_t i = 0; i < 3; i++) result[i] += matrix[i][k] * rhd.at(i);
    }
    return result;
}
