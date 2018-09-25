#ifndef VECTOR3D_H
#define VECTOR3D_H

#include <math.h>
#include <assert.h>
#include <ostream>

struct Vector3d {
public:
    double x, y, z;
public:
    double norm() {
        return sqrt(x*x + y*y + z*z);
    }
public:
    Vector3d operator+(const Vector3d& rhd) {
        return {this->x + rhd.x, this->y + rhd.y, this->z + rhd.z};
    }

    Vector3d operator+(const double rhd) {
        return {this->x + rhd, this->y + rhd, this->z + rhd};
    }

    Vector3d operator*(const double rhd) {
        return {this->x * rhd, this->y * rhd, this->z * rhd};
    }

    void operator*=(const double rhd) {
         this->x *= rhd; this->y *= rhd; this->z *= rhd;
    }

    void operator-=(const Vector3d& rhd) {
         this->x -= rhd.x; this->y -= rhd.y; this->z -= rhd.z;
    }

    void operator+=(const Vector3d& rhd) {
         this->x += rhd.x; this->y += rhd.y; this->z += rhd.z;
    }

    double& operator[](std::size_t id) {
        switch (id) {
            case 0: return x;
            case 1: return y;
            case 2: return z;
            default: abort(); //TODO:
        }
    }

    double at(std::size_t id) const { //TODO: !!!
        switch (id) {
            case 0: return x;
            case 1: return y;
            case 2: return z;
            default: abort(); //TODO:
        }
    }

    friend std::ostream& operator<<(std::ostream& ostr, const Vector3d& v) {
        ostr << "(";
        for (std::size_t i = 0; i < 3 - 1; i++) ostr << v.at(i) << ", ";
        ostr << v.at(3 - 1) << ")\n";
        return ostr;
    }

};

#endif // VECTOR3D_H
