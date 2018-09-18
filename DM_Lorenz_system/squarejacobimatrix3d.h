#ifndef SQUAREJACOBIMATRIX3D_H
#define SQUAREJACOBIMATRIX3D_H

#include <array>
#include <functional>
#include <vector3d.h>

class SquareJacobiMatrix3d {
private:
    using Row = std::array<double, 3>;
public:
    SquareJacobiMatrix3d();
    SquareJacobiMatrix3d(std::function<Vector3d (Vector3d)> f, Vector3d p);

    SquareJacobiMatrix3d& inverse();
public:
    Vector3d operator*(const Vector3d& rhd);
private:
    void gauss();             // A*x=b
    void back_substitution(); // R*x=b
private:
    std::array<Row, 3> matrix;
};

#endif // SQUAREJACOBIMATRIX3D_H
