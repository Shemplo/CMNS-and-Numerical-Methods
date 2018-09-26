#ifndef SQUAREJACOBIMATRIX3D_H
#define SQUAREJACOBIMATRIX3D_H

#include <array>
#include <functional>
#include <vector3d.h>
#include <iostream>
#include <iomanip>

class SquareJacobiMatrix3d {
private:
    static const std::size_t n = 3;
    using Row = std::array<double, n>;
    using Matrix = std::array<Row, n>;
public:
    SquareJacobiMatrix3d(std::function<Vector3d (Vector3d)> f, Vector3d p);

    SquareJacobiMatrix3d& inverse();
public:
    Vector3d operator*(const Vector3d& rhd);

    friend std::ostream& operator<<(std::ostream& ostr, const SquareJacobiMatrix3d& J) {
        ostr.precision(3);

        for(const Row& row : J.matrix) {
            for(auto item : row) ostr << std::setw(6) << std::setfill(' ') << item << " ";
            ostr << "\n";
        }
        return ostr;
    }
private:
    Row gauss(Matrix A, Row b);             // A*x=b
    Row back_substitution(Matrix R, Row b); // R*x=b, R - upper triangular

    void show_matix() {
        std::cout << "\n" <<  *this << "\n";
        std::cout.flush();
    }
private:
    Matrix matrix;
};

#endif // SQUAREJACOBIMATRIX3D_H
