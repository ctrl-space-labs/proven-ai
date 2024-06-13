// ** React Imports
import { Fragment, useState } from "react";
import { styled, useTheme } from "@mui/material/styles";

// ** MUI Imports
import Box from "@mui/material/Box";
import Card from "@mui/material/Card";
import Step from "@mui/material/Step";
import Grid from "@mui/material/Grid";
import Button from "@mui/material/Button";
import Select from "@mui/material/Select";
import Divider from "@mui/material/Divider";
import Stepper from "@mui/material/Stepper";
import MenuItem from "@mui/material/MenuItem";
import StepLabel from "@mui/material/StepLabel";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import InputLabel from "@mui/material/InputLabel";
import IconButton from "@mui/material/IconButton";
import CardContent from "@mui/material/CardContent";
import FormControl from "@mui/material/FormControl";
import OutlinedInput from "@mui/material/OutlinedInput";
import FormHelperText from "@mui/material/FormHelperText";
import InputAdornment from "@mui/material/InputAdornment";
import Chip from "@mui/material/Chip";
import Autocomplete from "@mui/material/Autocomplete";
import CustomRadioIcons from "src/@core/components/custom-radio/icons";

// ** Third Party Imports
import * as yup from "yup";
import toast from "react-hot-toast";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";

// ** Icon Imports
import Icon from "src/@core/components/icon";

// ** Custom Components Imports
import StepperCustomDot from "./StepperCustomDot";

// ** Styled Components
import StepperWrapper from "src/@core/styles/mui/stepper";

// Define the testing array with 20 titles
const testing = [
  { title: "Education" },
  { title: "Healthcare" },
  { title: "Finance" },
  { title: "Marketing" },
  { title: "Customer Service" },
  { title: "Human Resources" },
  { title: "Retail" },
  { title: "Manufacturing" },
  { title: "Transportation" },
  { title: "Agriculture" },
  { title: "Entertainment" },
  { title: "Real Estate" },
  { title: "Legal" },
  { title: "Energy" },
  { title: "Gaming" },
  { title: "Security" },
  { title: "Tourism" },
  { title: "E-commerce" },
  { title: "Telecommunications" },
  { title: "Insurance" },
];

const steps = [
  {
    title: "User",
    subtitle: "User Information",
  },
  {
    title: "Agent",
    subtitle: "Agent Information",
  },  
  {
    title: "Review and Complete",
    subtitle: "Data Use Policy",
  },
];

const defaultUserInformation = {
  email: "",
  username: "",
 
  profileLink: "",
};

const defaultAgentInformation = {
  agentPurpose: [],
};



const userSchema = yup.object().shape({
  username: yup.string().required(),
  email: yup.string().email().required(),
  
});

const agentSchema = yup.object().shape({
  // agentPurpose: yup.string().required(),
  agentPurpose: yup
    .array()
    .min(1, "At least one purpose is required")
    .required("Purpose is required"),
});



const StepperLinearWithValidation = () => {
  // ** States
  const theme = useTheme();
  const [activeStep, setActiveStep] = useState(0);
  const [selectedAccess, setSelectedAccess] = useState(
    true ? "private" : "public"
  );
  const [selectedCompensation, setSelectedCompensation] = useState(
    true ? "paid" : "free"
  );

  const Access = [
    {
      value: "private",
      title: "Private",
      content: "Only within team",
    },
    {
      value: "public",
      title: "Public",
      isSelected: true,
      content: "Anyone can use",
    },
  ];

  const Compensation = [
    {
      value: "Paid",
      title: "Paid",
      isSelected: true,
      content: "Anyone can use",
    },
    {
      value: "Free",
      title: "Free",
      content: "Only within team",
    },
  ];

  const AccessIcons = [
    {
      icon: "mdi:lock-open",
      iconProps: { fontSize: "2rem", style: { marginBottom: 8 } },
    },
    {
      icon: "mdi:lock",
      iconProps: { fontSize: "2rem", style: { marginBottom: 8 } },
    },
  ];

  const CompensationIcons = [
    {
      icon: "mdi:lock-open",
      iconProps: { fontSize: "2rem", style: { marginBottom: 8 } },
    },
    {
      icon: "mdi:lock",
      iconProps: { fontSize: "2rem", style: { marginBottom: 8 } },
    },
  ];

  const handleAccessChange = (prop) => {
    setSelectedAccess(typeof prop === "string" ? prop : prop.target.value);
  };

  const handleCompensationChange = (prop) => {
    setSelectedCompensation(typeof prop === "string" ? prop : prop.target.value);
  };

  const [state, setState] = useState({
    password: "",
    password2: "",
    showPassword: false,
    showPassword2: false,
  });

  // ** Hooks
  const {
    reset: userReset,
    control: userControl,
    handleSubmit: handleUserSubmit,
    formState: { errors: userErrors },
  } = useForm({
    defaultValues: defaultUserInformation,
    resolver: yupResolver(userSchema),
  });

  const {
    reset: agentReset,
    control: agentControl,
    handleSubmit: handleAgentSubmit,
    formState: { errors: agentErrors },
  } = useForm({
    defaultValues: defaultAgentInformation,
    resolver: yupResolver(agentSchema),
  });

  

  // Handle Stepper
  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  const handleReset = () => {
    setActiveStep(0);    
    userReset({
      email: "",
      username: "",
      
    });
    agentReset({
      country: "",
      language: [],
      "last-name": "",
      "first-name": "",
    });
  };

  const onSubmit = () => {
    console.log("Submitted");
    setActiveStep(activeStep + 1);
    console.log(activeStep);
    if (activeStep === steps.length - 1) {
      toast.success("Form Submitted");
    }
  };

  // // Handle Password


  const getStepContent = (step) => {
    switch (step) {
      // ***************************************************CASE 1 USER***************************************************
      case 0:
        return (
          <form key={0} onSubmit={handleUserSubmit(onSubmit)}>
            <Grid container spacing={5}>
              <Grid item xs={12}>
                <Typography
                  variant="body2"
                  sx={{ fontWeight: 600, color: "text.primary" }}
                >
                  {steps[0].subtitle}
                </Typography>
                <Typography variant="caption" component="p">
                  Enter Your Account Details
                </Typography>
              </Grid>

              <Grid item xs={12} sm={6}>
                <FormControl fullWidth>
                  <Controller
                    name="username"
                    control={userControl}
                    rules={{ required: true }}
                    render={({ field: { value, onChange } }) => (
                      <TextField
                        value={value || []}
                        label="Username"
                        onChange={onChange}
                        placeholder="carterLeonard"
                        error={Boolean(userErrors.username)}
                        aria-describedby="stepper-linear-account-username"
                      />
                    )}
                  />
                  {userErrors.username && (
                    <FormHelperText
                      sx={{ color: "error.main" }}
                      id="stepper-linear-account-username"
                    >
                      This field is required
                    </FormHelperText>
                  )}
                </FormControl>
              </Grid>
              <Grid item xs={12} sm={6}>
                <FormControl fullWidth>
                  <Controller
                    name="email"
                    control={userControl}
                    rules={{ required: true }}
                    render={({ field: { value, onChange } }) => (
                      <TextField
                        type="email"
                        value={value}
                        label="Email"
                        onChange={onChange}
                        error={Boolean(userErrors.email)}
                        placeholder="carterleonard@gmail.com"
                        aria-describedby="stepper-linear-account-email"
                      />
                    )}
                  />
                  {userErrors.email && (
                    <FormHelperText
                      sx={{ color: "error.main" }}
                      id="stepper-linear-account-email"
                    >
                      {userErrors.email.message}
                    </FormHelperText>
                  )}
                </FormControl>
              </Grid>
              
              <Grid item xs={12}>
                <FormControl fullWidth>
                  <Controller
                    name="profileLink"
                    control={userControl}
                    rules={{ required: true }}
                    render={({ field: { value, onChange } }) => (
                      <TextField
                        value={value}
                        label="Profile Link"
                        onChange={onChange}
                        placeholder="https://yourprofilelink.com"
                        error={Boolean(userErrors.profileLink)}
                        aria-describedby="stepper-linear-account-profileLink"
                      />
                    )}
                  />
                  {userErrors.profileLink && (
                    <FormHelperText
                      sx={{ color: "error.main" }}
                      id="stepper-linear-account-profileLink"
                    >
                      This field is required
                    </FormHelperText>
                  )}
                </FormControl>
              </Grid>

              <Grid
                item
                xs={12}
                sx={{ display: "flex", justifyContent: "space-between" }}
              >
                <Button
                  size="large"
                  variant="outlined"
                  color="secondary"
                  disabled
                >
                  Previous
                </Button>
                <Button size="large" type="submit" variant="contained">
                  Next
                </Button>
              </Grid>
            </Grid>
          </form>
        );
      // ***************************************************CASE 2 AGENT***************************************************
      case 1:
        return (
          <form key={1} onSubmit={handleAgentSubmit(onSubmit)}>
            <Grid container spacing={5}>
              <Grid item xs={12}>
                <Typography
                  variant="h4"
                  sx={{ fontWeight: 800, color: "text.primary" }}
                >
                  {steps[1].subtitle}
                </Typography>
                <Typography variant="subtitle2" component="p">
                  Enter Your Agent Details
                </Typography>
              </Grid>

              <Grid item xs={12}>
                <FormControl fullWidth>
                  <Typography
                    variant="filled"
                    sx={{ fontWeight: 600, color: "text.primary" }}
                  >
                    What is the purpose of use?
                  </Typography>

                  <Controller
                    name="agentPurpose"
                    control={agentControl}
                    render={({ field: { value, onChange } }) => (
                      <Autocomplete
                        multiple
                        freeSolo
                        sx={{ width: "40%", mt: 2 }}
                        id="autocomplete-multiple-filled-agentPurpose"
                        value={value}
                        onChange={(event, newValue) => onChange(newValue)}
                        options={testing.map((option) => option.title)}
                        renderInput={(params) => (
                          <TextField
                            {...params}
                            variant="filled"
                            // label="What is the purpose of use?"
                            placeholder="training"
                            sx={{ mb: 2, mt: 2 }}
                            error={!!agentErrors.agentPurpose}
                            helperText={
                              agentErrors.agentPurpose
                                ? agentErrors.agentPurpose.message
                                : ""
                            }
                          />
                        )}
                        renderTags={(value, getTagProps) =>
                          value.map((option, index) => (
                            <Chip
                              variant="outlined"
                              label={option}
                              {...getTagProps({ index })}
                              key={index}
                              sx={{ mr: 1, mb: 1, mt: 1 }}
                            />
                          ))
                        }
                      />
                    )}
                  />
                </FormControl>
              </Grid>

              <Grid item xs={12} sm={6}>
                <FormControl fullWidth>
                  <Typography
                    variant="body2"
                    sx={{ fontWeight: 600, color: "text.primary" }}
                  >
                    Access
                  </Typography>

                  <Controller
                    name="denyList"
                    control={agentControl}
                    render={({ field: { value, onChange } }) => (
                      <Grid container spacing={4} item xs={12} sm={12}>
                        {Access.map((item, index) => (
                          <CustomRadioIcons
                            data={Access[index]}
                            selected={selectedAccess}
                            icon={AccessIcons[index].icon}
                            name="custom-radios-icons"
                            handleChange={handleAccessChange}
                            gridProps={{ sm: 4, xs: 12 }}
                            iconProps={AccessIcons[index].iconProps}
                          />
                        ))}
                      </Grid>
                    )}
                  />
                </FormControl>
              </Grid>

              <Grid item xs={12} sm={6}>
                <FormControl fullWidth>
                  <Typography
                    variant="body2"
                    sx={{ fontWeight: 600, color: "text.primary" }}
                  >
                    Compensation
                  </Typography>

                  <Controller
                    name="denyList"
                    control={agentControl}
                    render={({ field: { value, onChange } }) => (
                      <Grid container spacing={4} item xs={12} sm={12}>
                        {Compensation.map((item, index) => (
                          <CustomRadioIcons
                            data={Compensation[index]}
                            selected={selectedCompensation}
                            icon={CompensationIcons[index].icon}
                            name="custom-radios-icons"
                            handleChange={handleCompensationChange}
                            gridProps={{ sm: 4, xs: 12 }}
                            iconProps={CompensationIcons[index].iconProps}
                          />
                        ))}
                      </Grid>
                    )}
                  />
                </FormControl>
              </Grid>

              

              <Grid
                item
                xs={12}
                sx={{ display: "flex", justifyContent: "space-between" }}
              >
                <Button
                  size="large"
                  variant="outlined"
                  color="secondary"
                  onClick={handleBack}
                >
                  Previous
                </Button>
                <Button size="large" type="submit" variant="contained">
                  Next
                </Button>
              </Grid>
            </Grid>
          </form>
        );
      
      // ***************************************************CASE 3 Review and Complete***************************************************
      case 2:
        return (
          <form key={2} onSubmit={handleAgentSubmit(onSubmit)}>
            <Grid container spacing={5}>
              <Grid item xs={12}>
                <Typography
                  variant="h4"
                  sx={{ fontWeight: 800, color: "text.primary" }}
                >
                  {steps[2].title}
                </Typography>
                <Typography variant="subtitle2" component="p">
                  Enter Your Agent Details
                </Typography>
              </Grid>
              <Grid item xs={12}>
                <Card>
                  <CardContent sx={{ padding: 60 }}></CardContent>
                </Card>
              </Grid>
              <Grid
                item
                xs={12}
                sx={{ display: "flex", justifyContent: "space-between" }}
              >
                <Button
                  size="large"
                  variant="outlined"
                  color="secondary"
                  onClick={handleBack}
                >
                  Previous
                </Button>
                <Button size="large" type="submit" variant="contained">
                  Submit
                </Button>
              </Grid>
            </Grid>
          </form>
        );
      default:
        return null;
    }
  };

  const renderContent = () => {
    if (activeStep === steps.length) {
      return (
        <Fragment>
          <Typography>All steps are completed!</Typography>
          <Box sx={{ mt: 4, display: "flex", justifyContent: "flex-end" }}>
            <Button size="large" variant="contained" onClick={handleReset}>
              Reset
            </Button>
          </Box>
        </Fragment>
      );
    } else {
      return getStepContent(activeStep);
    }
  };

  return (
    <Card sx={{ backgroundColor: "action.hover" }}>
      <CardContent>
        <StepperWrapper>
          <Stepper activeStep={activeStep}>
            {steps.map((step, index) => {
              const labelProps = {};
              if (index === activeStep) {
                labelProps.error = false;
                if (
                  (userErrors.email ||
                    userErrors.username ) &&
                  activeStep === 0
                ) {
                  labelProps.error = true;
                } else if (agentErrors.agentPurpose && activeStep === 1) {
                  labelProps.error = true;
                }  else {
                  labelProps.error = false;
                }
              }

              return (
                <Step key={index}>
                  <StepLabel
                    {...labelProps}
                    StepIconComponent={StepperCustomDot}
                  >
                    <div className="step-label">
                      <Typography className="step-number">{`0${
                        index + 1
                      }`}</Typography>
                      <div>
                        <Typography className="step-title">
                          {step.title}
                        </Typography>
                        <Typography className="step-subtitle">
                          {step.subtitle}
                        </Typography>
                      </div>
                    </div>
                  </StepLabel>
                </Step>
              );
            })}
          </Stepper>
        </StepperWrapper>
      </CardContent>

      <Divider sx={{ m: "0 !important" }} />

      <CardContent>{renderContent()}</CardContent>
    </Card>
  );
};

export default StepperLinearWithValidation;
