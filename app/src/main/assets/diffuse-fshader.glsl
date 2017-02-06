precision mediump float;

uniform vec3 uLight, uLight2, uColor;

varying vec3 vNormal;
varying vec3 vPosition;

void main() {
    vec3 toLight = normalize(uLight - vPosition);
    vec3 normal = normalize(vNormal);

    // diffuse
    float diffuse = max(0.0, dot(normal, toLight));
    vec3 intensity = uColor * diffuse;

    gl_FragColor = vec4(intensity, 1.0);
}
