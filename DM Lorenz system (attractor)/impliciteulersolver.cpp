#include "impliciteulersolver.h"

ImplicitEulerSolver::ImplicitEulerSolver(double x0, double y0, double z0, double sigma, double r, double b, double dt, std::string name)
    : SystemSolver (x0, y0, z0, sigma, r, b, dt, name)
{

}


#include "expliciteulersolver.h"
std::vector<double> *ImplicitEulerSolver::solve(double time) {
    std::size_t n_ticks = std::size_t(time / dt);

    std::vector<point3d> work_f(n_ticks + 1);
    work_f[0] = {x0, y0, z0};

    ExplicitEulerSolver tmp(x0, y0, z0, sigma, r, b, dt, "Explicit Euler");
    auto next = tmp.solve(2 * time);

    for(std::size_t tick = 0; tick < n_ticks; tick++) {
        static point3d p; // <-- work_f[tick + 1];
        p.x = next[0][tick + 1];
        p.y = next[1][tick + 1];
        p.z = next[2][tick + 1];

        work_f[tick + 1].x = work_f[tick].x + dt * x(p); // <-- dx
        work_f[tick + 1].y = work_f[tick].y + dt * y(p); // <-- dy
        work_f[tick + 1].z = work_f[tick].z + dt * z(p); // <-- dz
    }

    return transform_result(work_f);
}

std::vector<double> *ImplicitEulerSolver::transform_result(std::vector<point3d> &work_f) {
    std::vector<double> *triple = new std::vector<double> [DIMENSION];
    for (int i = 0; i < DIMENSION; i++) {
        triple [i] = std::vector<double> ();
    }

    for(std::size_t i = 0; i < work_f.size(); i++) {
       triple[0].emplace_back(work_f[i].x);
       triple[1].emplace_back(work_f[i].y);
       triple[2].emplace_back(work_f[i].z);
    }
    return triple;
}
