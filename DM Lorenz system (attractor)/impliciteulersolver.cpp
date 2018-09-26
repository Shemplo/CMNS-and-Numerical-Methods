#include "impliciteulersolver.h"

ImplicitEulerSolver::ImplicitEulerSolver(InputParams input, std::string name)
    : SystemSolver (input, name)
{

}


std::vector<double> *ImplicitEulerSolver::solve(double time) {
    std::size_t n_ticks = std::size_t(time / dt);
    static constexpr double EPS = 1e-5;

    std::vector<Vector3d> work_f(n_ticks + 1);
    work_f[0] = {x0, y0, z0};

    std::function<Vector3d(Vector3d, Vector3d)> f = [&](Vector3d p, Vector3d p0) {
        static const double dt_sigma = dt * sigma;

        double f1 = p0.x - p.x * (1 + dt_sigma) + dt_sigma * p.y,
               f2 = p0.y + dt * p.x * (r - p.z) - p.y * (1 + dt),
               f3 = p0.z + dt * p.x * p.y - p.z * (1 + dt * b);
        return Vector3d{f1, f2, f3};
    };

    for(std::size_t tick = 1; tick <= n_ticks; tick++) {
        work_f[tick] = newton_method(std::bind(f, std::placeholders::_1,
                           work_f[tick - 1]), work_f[tick - 1], EPS , 20);
    }

    return transform_result(work_f);
}

std::vector<double> *ImplicitEulerSolver::transform_result(std::vector<Vector3d> &work_f) {
    static constexpr int dimension = 3;
    std::vector<double> *triple = new std::vector<double> [dimension];
    for (int i = 0; i < dimension; i++) {
        triple [i] = std::vector<double> ();
    }

    for(std::size_t i = 0; i < work_f.size(); i++) {
       triple[0].emplace_back(work_f[i].x);
       triple[1].emplace_back(work_f[i].y);
       triple[2].emplace_back(work_f[i].z);
    }
    return triple;
}

Vector3d ImplicitEulerSolver::newton_method(std::function<Vector3d (Vector3d)> f,
        Vector3d x_0, double eps, std::size_t n_iterations) {
    Vector3d x_k = x_0;

    for (std::size_t i = 0; i < n_iterations && eps < f(x_k).norm(); i++) {
        SquareJacobiMatrix3d m(f, x_k);
        x_k -= m.inverse() * f(x_k);
    }

    return x_k;
}
