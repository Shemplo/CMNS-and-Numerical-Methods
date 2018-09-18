#include "rungekuttasolver.h"

RungeKuttaSolver::RungeKuttaSolver(InputParams input, std::string name)
    : SystemSolver (input, name)
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

    double k[5][3] = {}, dy;
    double coeffs[4] = { 0.0, 0.5, 0.5, 1.0 };

    while (currentTime < time - dt) {
        double xk[3] = { triple[0][gen], triple[1][gen], triple[2][gen] };

        for (unsigned int i = 1; i < 5; i++) {
            k[i][0] = x(xk[0] + coeffs[i] * dt * k[i - 1][0], xk[1] + coeffs[i] * dt * k[i - 1][1], xk[2] + coeffs[i] * dt * k[i - 1][2]);
            k[i][1] = y(xk[0] + coeffs[i] * dt * k[i - 1][0], xk[1] + coeffs[i] * dt * k[i - 1][1], xk[2] + coeffs[i] * dt * k[i - 1][2]);
            k[i][2] = z(xk[0] + coeffs[i] * dt * k[i - 1][0], xk[1] + coeffs[i] * dt * k[i - 1][1], xk[2] + coeffs[i] * dt * k[i - 1][2]);
        }

        for (unsigned i = 0; i < 3; i++) {
            dy = (1.0/6.0) * (k[1][i] + 2 * k[2][i] + 2 * k[3][i] + k[4][i]);
            triple[i].push_back(xk[i] + dt * dy);
        }

        currentTime += dt;
        gen += 1;
    }


    return triple;
}

RungeKuttaSolver::~RungeKuttaSolver() {}
