#version 330
 
layout (std140) uniform Matrices {
    mat4 m_pvm;
    mat4 m_viewModel;
    mat3 m_normal;
};
 
in vec4 position;   // local space
in vec3 normal;     // local space
 
// the data to be sent to the fragment shader
out Data {
    vec3 normal;
    vec4 eye;
} DataOut;
 
void main () {
 
    DataOut.normal = normalize(m_normal * normal);
    DataOut.eye = -(m_viewModel * position);
 
    gl_Position = m_pvm * position; 
}