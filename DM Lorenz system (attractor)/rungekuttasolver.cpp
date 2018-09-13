#include "rungekuttasolver.h"

RungeKuttaSolver::RungeKuttaSolver(double x0, double y0, double z0, double sigma, double r, double b, double dt, std::string name)
    : SystemSolver (x0, y0, z0, sigma, r, b, dt, name)
{

}

std::vector<double> *RungeKuttaSolver::solve(double time) {
    std::vector<double> *triple = new std::vector<double> [DIMENSION];
    for (int i = 0; i < DIMENSION; i++) {
        triple [i] = std::vector<double> ();
    }

    triple [0].push_back(x0); triple [1].push_back(y0); triple [2].push_back(z0);

    double currentTime = 0;
    unsigned int gen = 0;

    while (currentTime < time - dt) {
        double xk = triple [0][gen], yk = triple [1][gen], zk = triple [2][gen];

        double k1x, k1y, k1z,
               k2x, k2y, k2z,
               k3x, k3y, k3z,
               k4x, k4y, k4z,
               dyx, dyy, dyz;

        k1x = x(xk, yk, zk);
        k1y = y(xk, yk, zk);
        k1z = z(xk, yk, zk);

        k2x = x(xk + 0.5 * dt, yk + 0.5 * dt, zk + 0.5 * dt);
        k2y = y(xk + 0.5 * dt, yk + 0.5 * dt, zk + 0.5 * dt);
        k2z = z(xk + 0.5 * dt, yk + 0.5 * dt, zk + 0.5 * dt);

        k3x = x(xk + 0.5 * dt, yk + 0.5 * dt, zk + 0.5 * dt);
        k3y = y(xk + 0.5 * dt, yk + 0.5 * dt, zk + 0.5 * dt);
        k3z = z(xk + 0.5 * dt, yk + 0.5 * dt, zk + 0.5 * dt);

        k4x = x(xk + dt, yk + dt, zk + dt);
        k4y = y(xk + dt, yk + dt, zk + dt);
        k4z = z(xk + dt, yk + dt, zk + dt);

        dyx = (1.0/6.0) * (k1x + 2 * k2x + 2 * k3x + k4x);
        dyy = (1.0/6.0) * (k1y + 2 * k2y + 2 * k3y + k4y);
        dyz = (1.0/6.0) * (k1z + 2 * k2z + 2 * k3z + k4z);

        triple[0].push_back(xk + dt * dyx);
        triple[1].push_back(yk + dt * dyy);
        triple[2].push_back(zk + dt * dyz);

        currentTime += dt;
        gen += 1;
    }


    return triple;
}

RungeKuttaSolver::~RungeKuttaSolver() {}
