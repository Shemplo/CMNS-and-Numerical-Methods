#include "squarejacobimatrix3d.h"

SquareJacobiMatrix3d::SquareJacobiMatrix3d(std::function<Vector3d (Vector3d)> f, Vector3d p) {
    double h;
    {
        double max = 1;
        for (std::size_t i = 0; i < 3; i++) max = std::max(max, fabs(p[i]));
        h = max * 1e-5;
    }
    Vector3d f_eval_0 = f(p), f_eval_1;
    Vector3d p1 = p;

    for (std::size_t k = 0; k < 3; k++) {
        p1[k] += h;   // Do delta
        f_eval_1 = f(p1);
        for (std::size_t i = 0; i < 3; i++) matrix[i][k] = (f_eval_1[i] - f_eval_0[i]) / h;
        p1[k] = p[k]; // Undo delta
    }
}

SquareJacobiMatrix3d &SquareJacobiMatrix3d::inverse() {
    Matrix I;
    for (std::size_t i = 0; i < I.size(); ++i) I[i][i] = 1;

    Matrix result;
    for (std::size_t i = 0; i < I.size(); ++i) {
        result[i] = gauss(matrix, I[i]);
    }

    matrix = result;
    return *this;
}

Vector3d SquareJacobiMatrix3d::operator*(const Vector3d &rhd) {
    Vector3d result;
    for (std::size_t k = 0; k < 3; k++) {
        for(std::size_t i = 0; i < 3; i++) result[i] += matrix[i][k] * rhd.at(i);
    }
    return result;
}

SquareJacobiMatrix3d::Row SquareJacobiMatrix3d::
        gauss(SquareJacobiMatrix3d::Matrix A, SquareJacobiMatrix3d::Row b) {
    // Solve A * x = b; Return x;
    Matrix R = A;
    Row c = b;

    // Хотим верхнетреугольную
    for (std::size_t j = 0; j < n; j++) {
        std::size_t pivot = j;
        double max_entry = fabs(R[j][j]);
        for (std::size_t i = j + 1; i < n; i++) {
            double entry = fabs(R[i][j]);
            if (entry > max_entry) {
                max_entry = entry;
                pivot = i;
            }
        }

        if (pivot != j) {
            double temp;
            for (std::size_t i = j; i < n; i++) {
                std::swap(R[j][i], R[pivot][i]);
                temp = R[j][i];
                R[j][i] = R[pivot][i];
                R[pivot][i] = temp;
            }
            temp = c[j];
            c[j] = c[pivot];
            c[pivot] = temp;
        }

        for (std::size_t i = j + 1; i < n; i++) {
            double l = R[i][j] / R[j][j];
            for (std::size_t k = j + 1; k < n; k++) {
                R[i][k] = R[i][k] - l * R[j][k];
            }
            c[i] = c[i] - l * c[j];
        }
    }

    return back_substitution(std::move(R), std::move(c));
}

SquareJacobiMatrix3d::Row SquareJacobiMatrix3d::back_substitution(SquareJacobiMatrix3d::Matrix R, SquareJacobiMatrix3d::Row b) {
    Row x;//Why ?
    for (int i = n - 1; i >= 0; i--) {
        x[i] = b[i];
        for (std::size_t j = i + 1; j < n; j++) {
            x[i] = x[i] - R[i][j] * x[j];
        }
        x[i] = x[i] / R[i][i];
    }
    return x;
}
